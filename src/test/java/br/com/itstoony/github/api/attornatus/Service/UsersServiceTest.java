package br.com.itstoony.github.api.attornatus.Service;

import br.com.itstoony.github.api.attornatus.model.Address;
import br.com.itstoony.github.api.attornatus.model.Users;
import br.com.itstoony.github.api.attornatus.model.dto.UserRecord;
import br.com.itstoony.github.api.attornatus.model.dto.UsersDto;
import br.com.itstoony.github.api.attornatus.repository.UserRepository;
import br.com.itstoony.github.api.attornatus.service.AddressService;
import br.com.itstoony.github.api.attornatus.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsersServiceTest {

    UserService usersService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AddressService addressService;


    @BeforeEach
    public void setUp() {
        this.usersService = new UserService(userRepository);
        usersService.setAddressService(addressService);
    }

    @Test
    @DisplayName("Should insert an user using a record")
    public void insertByRecordTest() {
        // scenary
        UserRecord record = createRecord();

        Mockito.when(addressService.findByCep(record.cep())).thenReturn(createAddress());

        // execution
        Users returnedUser = usersService.insertByRecord(record);

        // validation
        assertThat(returnedUser.getAddress().get(0).getId()).isNotNull();
        assertThat(returnedUser.getAddress().get(0).getZipcode()).isEqualTo(record.cep());
        assertThat(returnedUser.getAddress().get(0).getHouseNumber()).isEqualTo(record.number());
        assertThat(returnedUser.getBirthDay()).isEqualTo(record.birthDay());
        assertThat(returnedUser.getRegistrationDate()).isNotNull();

    }

    @Test
    @DisplayName("Should insert an user in database")
    public void saveUserTest() {
        // scenary
        Users user = createUsers();
        Mockito.when(usersService.insert(user)).thenReturn(
                Users.builder()
                        .id(1L)
                        .name("John")
                        .birthDay(LocalDate.of(2000, 1, 2))
                        .registrationDate(LocalDate.now())
                        .address(new ArrayList<>())
                        .build());

        // execution
        var savedUser = usersService.insert(user);

        // validation
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(savedUser.getName()).isEqualTo("John");
        assertThat(savedUser.getBirthDay()).isEqualTo(LocalDate.of(2000, 1, 2));
        assertThat(savedUser.getRegistrationDate()).isEqualTo(LocalDate.now());
        assertThat(savedUser.getAddress()).isEqualTo(new ArrayList<>());

    }

    @Test
    @DisplayName("Should get an user by its id")
    public void findByIdTest() {
        // scenary
        Long id = 1L;
        Users user = createUsers();
        user.setId(id);

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // execution
        Users foundUser = usersService.findById(id);

        // validation
        assertThat(foundUser.getId()).isEqualTo(id);
        assertThat(foundUser.getName()).isEqualTo(user.getName());
        assertThat(foundUser.getRegistrationDate()).isEqualTo(user.getRegistrationDate());
        assertThat(foundUser.getAddress()).isEqualTo(user.getAddress());
        assertThat(foundUser.getBirthDay()).isEqualTo(user.getBirthDay());

    }

    @Test
    @DisplayName("Should throw an exception when id is invalid")
    public void userNotFoundTest() throws EntityNotFoundException {
        // scenary
        Long id = 1L;
        String errorMessage = "Invalid User ID";
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        // execution
        Throwable exception = catchThrowable(() -> usersService.findById(id));

        // validation
        assertThat(exception)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(errorMessage);
    }

    @Test
    @DisplayName("Should update an user")
    public void updateUserTest() {
        // scenary
        Users user = createUsers();
        UserRecord record = createRecord();

        // execution
        usersService.update(user, record);

        // validation
        assertThat(user.getName()).isEqualTo(record.name());
        assertThat(user.getBirthDay()).isEqualTo(record.birthDay());
    }

    @Test
    @DisplayName("Should convert an user into a DTO")
    public void toDtoTest() {
        // scenary
        Users user = createUsers();

        // execution
        UsersDto dto = usersService.toDto(user);

        // validation
        assertThat(dto.getId()).isEqualTo(user.getId());
        assertThat(dto.getAddressList()).isEqualTo(user.getAddress());
        assertThat(dto.getBirthDay()).isEqualTo(user.getBirthDay());
        assertThat(dto.getName()).isEqualTo(user.getName());
    }

    @Test
    @DisplayName("Should add an address to an users addresslist")
    public void addAddressTest() {
        // scenary
        Users user = createUsers();
        UserRecord record = createRecord();

        Mockito.when(addressService.findByCep(record.cep())).thenReturn(createAddress());
        // execution
        Address address = usersService.addAddress(user, record);

        // validation
        assertThat(user.getAddress()).contains(address);
        assertThat(user.getAddress().get(0).getZipcode()).isEqualTo(record.cep());
        assertThat(user.getAddress().get(0).getHouseNumber()).isEqualTo(record.number());
    }

    @Test
    @DisplayName("Should set an address to main")
    public void setMainAddressTest() {
        // scenary
        Users user = createUsers();
        Address ad1 = createAddress();
        Address ad2 = createAddress();
        ad2.setHouseNumber("22");

        user.getAddress().add(ad1);
        user.getAddress().add(ad2);

        // execution
        usersService.setMainAddress(user, ad2);

        // validation
        assertThat(ad1.getMain()).isFalse();
        assertThat(ad2.getMain()).isTrue();
    }


    public static Users createUsers() {
        return Users.builder()
                .name("John")
                .birthDay(LocalDate.of(2000, 1, 2))
                .registrationDate(LocalDate.now())
                .address(new ArrayList<>())
                .build();
    }

    public static UserRecord createRecord() {
        return new UserRecord("Mary", LocalDate.of(1998, 5, 2), "73050000", "52");
    }

    public static Address createAddress() {
        return Address.builder()
                .id(1L)
                .users(null)
                .city("NI")
                .states("")
                .street("")
                .neighborhood("")
                .zipcode("73050000")
                .houseNumber("52")
                .build();
    }
}
