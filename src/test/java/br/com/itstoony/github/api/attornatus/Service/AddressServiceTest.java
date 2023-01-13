package br.com.itstoony.github.api.attornatus.Service;

import br.com.itstoony.github.api.attornatus.model.Address;
import br.com.itstoony.github.api.attornatus.repository.AddressRepository;
import br.com.itstoony.github.api.attornatus.service.AddressService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AddressServiceTest {

    AddressService addressService;

    @MockBean
    AddressRepository addressRepository;


    @BeforeEach
    public void setUp() {
        this.addressService = new AddressService(addressRepository);
    }

    @Test
    @DisplayName("Should create an address using a CEP")
    public void findByCepTest() {
        // scenary
        String cep = "73050000";

        // execution
        Address address = addressService.findByCep(cep);

        // validation
        assertThat(address.getMain()).isNull();
        assertThat(address.getZipcode().replace("-", "")).isEqualTo(cep);
        assertThat(address.getHouseNumber()).isNull();
        assertThat(address.getNeighborhood()).isNotEmpty();
        assertThat(address.getCity()).isNotEmpty();
        assertThat(address.getStates()).isNotEmpty();
        assertThat(address.getStreet()).isNotEmpty();
        assertThat(address.getUsers()).isNull();
    }

    @Test
    @DisplayName("Should insert an address in database")
    public void insertAddressTest() {
        // scenary
        Address address = createAddress();
        Address returned = Address.builder()
                .id(1L).users(null).city("NI")
                .states("")
                .zipcode("73050000")
                .houseNumber("52")
                .build();

        Mockito.when(addressRepository.save(address)).thenReturn(returned);

        // execution
        addressService.insert(address);

        // validation
        assertThat(returned.getId()).isNotNull();
    }

    @Test
    @DisplayName("Should find an address by its id")
    public void findByIdTest() {
        // scenary
        Long id = 1L;

        Mockito.when(addressRepository.findById(id)).thenReturn(Optional.of(createAddress()));

        // execution
        Address found = addressService.findById(id);

        // validation
        assertThat(found.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Should throw an error when trying to find address by invalid id")
    public void addressNotFoundTest() throws EntityNotFoundException {
        // scenary
        Long id = 1L;
        String errorMessage = "Invalid Address ID";
        Mockito.when(addressRepository.findById(id)).thenReturn(Optional.empty());

        // execution
        Throwable exception = catchThrowable(() -> addressService.findById(id));

        // validation
        assertThat(exception)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(errorMessage);
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
