package br.com.itstoony.github.api.attornatus.Service;

import br.com.itstoony.github.api.attornatus.model.Users;
import br.com.itstoony.github.api.attornatus.model.dto.UserRecord;
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

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

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
    @DisplayName("Should insert an user in database")
    public void saveUserTest() {

        // scenary
        var user = createUsers();
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
        assertThat(savedUser.getName()).isEqualTo("John");
        assertThat(savedUser.getBirthDay()).isEqualTo(LocalDate.of(2000, 1, 2));
        assertThat(savedUser.getRegistrationDate()).isEqualTo(LocalDate.now());
        assertThat(savedUser.getAddress()).isEqualTo(new ArrayList<>());
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
        return new UserRecord("John", LocalDate.of(2000, 1, 2), "73050000", "52");
    }
}
