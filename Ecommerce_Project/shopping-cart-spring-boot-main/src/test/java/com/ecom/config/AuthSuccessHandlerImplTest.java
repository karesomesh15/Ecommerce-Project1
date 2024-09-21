package com.ecom.config;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthSuccessHandlerImplTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthSucessHandlerImpl authSuccessHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOnAuthenticationSuccessAdmin() throws IOException, ServletException {
        // Given
        String username = "adminUser";
        Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // Mock authentication object to return the username and roles
        when(authentication.getName()).thenReturn(username);
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities); // Explicit wildcard capture

        // When
        authSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // Then
        verify(response).sendRedirect("/admin/");
        verify(response, never()).sendRedirect("/");
    }

    @Test
    public void testOnAuthenticationSuccessUser() throws IOException, ServletException {
        // Given
        String username = "normalUser";
        Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));

        // Mock authentication object to return the username and roles
        when(authentication.getName()).thenReturn(username);
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities); // Explicit wildcard capture

        // When
        authSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // Then
        verify(response).sendRedirect("/");
        verify(response, never()).sendRedirect("/admin/");
    }
}
