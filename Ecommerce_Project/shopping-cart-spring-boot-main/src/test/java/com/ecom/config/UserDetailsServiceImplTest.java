package com.ecom.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ecom.dao.UserDao;
import com.ecom.model.UserDtls;

public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserDao userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        // Arrange
        String email = "test@example.com";
        UserDtls user = new UserDtls();
        user.setEmail(email);
        user.setPassword("password");
        // Mock the behavior of the user repository
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String email = "notfound@example.com";

        // Mock the behavior of the user repository to return null
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException thrown = assertThrows(
            UsernameNotFoundException.class,
            () -> userDetailsService.loadUserByUsername(email)
        );

        // Assert that the message matches exactly what is thrown by the service
        assertEquals("user not found", thrown.getMessage());

        // Verify that the userRepository's findByEmail was called once with the email
        verify(userRepository, times(1)).findByEmail(email);
    }


}