package com.ecom.config;

import com.ecom.model.UserDtls;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class CustomUserTest {

    private CustomUser customUser;
    private UserDtls userDtls;

    @BeforeEach
    public void setUp() {
        userDtls = new UserDtls();
        userDtls.setEmail("test@example.com");
        userDtls.setPassword("password");
        userDtls.setRole("ROLE_USER");
        userDtls.setAccountNonLocked(true);
        userDtls.setIsEnable(true);

        customUser = new CustomUser(userDtls);
    }

    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = customUser.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_USER", authorities.iterator().next().getAuthority());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", customUser.getPassword());
    }

    @Test
    public void testGetUsername() {
        assertEquals("test@example.com", customUser.getUsername());
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(customUser.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(customUser.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(customUser.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        userDtls.setIsEnable(false);  // Change state to test
        customUser = new CustomUser(userDtls);
        assertFalse(customUser.isEnabled());

        userDtls.setIsEnable(true);  // Reset state
        customUser = new CustomUser(userDtls);
        assertTrue(customUser.isEnabled());
    }
}