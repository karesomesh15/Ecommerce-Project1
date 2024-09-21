package com.ecom.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;

import com.ecom.dao.UserDao;
import com.ecom.model.UserDtls;
import com.ecom.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

class AuthFailureHandlerImplTest {

    @Mock
    private UserDao userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthFailureHandlerImpl authFailureHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnAuthenticationFailureWithLockedAccount() throws Exception {
        // Setup mock objects
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        AuthenticationException exception = mock(LockedException.class);

        // Mock user details
        UserDtls userDtls = new UserDtls();
        userDtls.setFailedAttempt(3);
        userDtls.setAccountNonLocked(false);
        userDtls.setIsEnable(true); // Ensure this is not null

        when(request.getParameter("username")).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(userDtls);
        when(request.getSession()).thenReturn(session);

        // Test authentication failure
        authFailureHandler.onAuthenticationFailure(request, response, exception);

        // Verify the redirect to sign-in with error
        verify(response).sendRedirect("/signin?error");
    }

    @Test
    void testOnAuthenticationFailureWithInvalidEmail() throws Exception {
        // Setup mock objects
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        AuthenticationException exception = new LockedException("Email & password invalid");

        when(request.getParameter("username")).thenReturn("invalid@example.com");
        when(userRepository.findByEmail("invalid@example.com")).thenReturn(null); // Invalid email
        when(request.getSession()).thenReturn(session);

        // Test authentication failure
        authFailureHandler.onAuthenticationFailure(request, response, exception);

        // Verify the redirect to sign-in with error
        verify(response).sendRedirect("/signin?error");
    }
}
