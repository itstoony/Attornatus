package br.com.itstoony.github.api.attornatus.Repository;

import br.com.itstoony.github.api.attornatus.model.Address;
import br.com.itstoony.github.api.attornatus.repository.AddressRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class AddressRepositoryTest {

    @Autowired
    AddressRepository addressRepository;


    @Test
    @DisplayName("Should insert an address in database")
    public void insertAddress() {

        // scenary
        var address = addressBuilder();

        // execution
        var insertedAddress = addressRepository.save(address);

        // validation
        Assertions.assertThat(insertedAddress.getId()).isEqualTo(address.getId());

    }

    @Test
    @DisplayName("Should find an address by id")
    public void findById() {

        // scenary
        var address = addressBuilder();
        var saved = addressRepository.save(address);
        // execution
        var found = addressRepository.findById(saved.getId());

        // validation
        Assertions.assertThat(found.isPresent()).isTrue();
        Assertions.assertThat(found.get().getId()).isNotNull();
    }

    public static Address addressBuilder() {
        return Address.builder()
                .id(1L)
                .city("Natal")
                .houseNumber("412")
                .main(true)
                .neighborhood("Pajuçara")
                .states("RN")
                .street("Rua São Jorge")
                .zipcode("59123-427")
                .users(null)
                .build();
    }

}
