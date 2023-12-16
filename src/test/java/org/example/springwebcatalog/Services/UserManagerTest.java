package org.example.springwebcatalog.Services;

import org.example.springwebcatalog.Repositories.UserRepository;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.example.springwebcatalog.Model.User.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserManagerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserManager userManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("registerUser - password encoded")
    void registerUser_shouldEncodePassword() {
        // Arrange
        CustomUser user = new CustomUser("testuser", "password", Role.USER);

        // Act
        userManager.registerUser(user, Role.USER);

        // Assert
        verify(userRepository, times(1)).save(user);
        assertThat(user.getPassword()).isNotEqualTo("password");
    }

    @Test
    @DisplayName("find user by name - should find")
    void findUserByName_shouldEquals() {
        // Arrange
        String username = "testuser";
        CustomUser user = new CustomUser(username, "password", Role.USER);
        when(userRepository.findCustomUserByUsername(username)).thenReturn(user);

        // Act
        CustomUser result = userManager.findUserByName(username);

        // Assert
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("logout - invalidate session")
    void logout_shouldInvalidateSession() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(auth);

        // Act
        userManager.logout(request);

        // Assert
        verify(securityContext, times(1)).getAuthentication(); // Ensure getAuthentication is called on the SecurityContext
        verify(securityContext, times(1)).setAuthentication(null); // Ensure invalidate is called on the same session instance
    }


    @Test
    @DisplayName("loadUserByUsername - existing user case")
    void loadUserByUsername_existingUser() {
        // Arrange
        String username = "testuser";
        CustomUser user = new CustomUser(username, "encodedPassword", Role.USER);
        when(userRepository.findCustomUserByUsername(username)).thenReturn(user);

        // Act
        UserDetails userDetails = userManager.loadUserByUsername(username);

        // Assert
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword");
        // Add more assertions as needed based on your specific requirements
    }

    @Test
    @DisplayName("loadUserByUsername - non existing user case")
    void loadUserByUsername_nonExistingUser() {
        // Arrange
        String username = "nonExistingUser";
        when(userRepository.findCustomUserByUsername(username)).thenReturn(null);

        // Act and Assert
        org.junit.jupiter.api.Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> userManager.loadUserByUsername(username)
        );
    }
}
