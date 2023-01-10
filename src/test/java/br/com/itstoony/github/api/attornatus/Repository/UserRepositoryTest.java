package br.com.itstoony.github.api.attornatus.Repository;

import br.com.itstoony.github.api.attornatus.model.Users;
import br.com.itstoony.github.api.attornatus.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should return true when insert a valid user in database")
    public void insertUserTest() {

        // scenary
        var user = usersBuilder();

        // execution
        Users insertedUser = userRepository.save(user);

        // validation
        Assertions.assertThat(insertedUser.getId()).isNotNull();
    }

    @Test
    @DisplayName("Should find users by id")
    public void findByIdTest() {

        // scenary
        var user = usersBuilder();
        var saved = userRepository.save(user);

        // execution
        var found = userRepository.findById(saved.getId());

        // validation
        Assertions.assertThat(found.isPresent()).isTrue();
        Assertions.assertThat(found.get().getId()).isEqualTo(saved.getId());
    }

    private static Users usersBuilder() {
        return Users.builder()
                .id(1L)
                .name("José")
                .birthDay(LocalDate.of(1995, 9, 23))
                .address(new ArrayList<>())
                .registrationDate(LocalDate.now())
                .build();
    }
}
