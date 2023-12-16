package org.example.springwebcatalog.Mapper;

import org.example.springwebcatalog.Model.User.CustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByUsername - should find in DB")
    public void findByUsername_ShouldEquals() {
        // Arrange
        CustomUser user = new CustomUser();
        user.setUsername("testUser");
        userRepository.save(user);

        // Act
        CustomUser foundUser = userRepository.findCustomUserByUsername("testUser");

        // Assert
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("testUser");
    }
}
