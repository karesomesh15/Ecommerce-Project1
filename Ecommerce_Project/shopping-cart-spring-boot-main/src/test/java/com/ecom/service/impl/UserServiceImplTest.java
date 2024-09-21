package com.ecom.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;

import com.ecom.dao.UserDao;
import com.ecom.model.UserDtls;
import com.ecom.util.AppConstant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDao userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MultipartFile img;

    private UserDtls user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserDtls();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setName("Test User");
    }

    @Test
    void testSaveUser() {
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserDtls.class))).thenReturn(user);

        UserDtls savedUser = userService.saveUser(user);
        assertNotNull(savedUser);
        assertEquals("Test User", savedUser.getName());
        assertEquals("ROLE_USER", savedUser.getRole());
    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        UserDtls foundUser = userService.getUserByEmail("test@example.com");
        assertNotNull(foundUser);
        assertEquals("Test User", foundUser.getName());
    }

    @Test
    void testUpdateAccountStatus() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Boolean updated = userService.updateAccountStatus(1, false);
        assertTrue(updated);
        assertFalse(user.getIsEnable());
    }

    @Test
    void testUnlockAccountTimeExpired() {
        user.setLockTime(new Date(System.currentTimeMillis() - (AppConstant.UNLOCK_DURATION_TIME + 1000))); // Simulate expiration
        user.setAccountNonLocked(false);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        boolean unlocked = userService.unlockAccountTimeExpired(user);
        assertTrue(unlocked);
        assertTrue(user.getAccountNonLocked());
        assertEquals(0, user.getFailedAttempt());
    }

	/*
	 * @Test void testUpdateUserProfile_UserNotFound() {
	 * when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
	 * 
	 * UserDtls updatedUser = userService.updateUserProfile(user, img);
	 * assertNull(updatedUser); // Should return null since user doesn't exist }
	 */

    @Test
    void testUpdateUserProfile() throws Exception {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(img.isEmpty()).thenReturn(false);
        when(img.getOriginalFilename()).thenReturn("profile.jpg");

        UserDtls updatedUser = userService.updateUserProfile(user, img);
        assertNotNull(updatedUser);
        assertEquals("profile.jpg", updatedUser.getProfileImage());
    }
}
