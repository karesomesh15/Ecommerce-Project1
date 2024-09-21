package com.ecom.controller;

import com.ecom.model.Category;
import com.ecom.model.UserDtls;
import com.ecom.service.CartService;
import com.ecom.service.CategoryService;
import com.ecom.service.UserService;
import com.ecom.util.CommonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CartService cartService;

    @Mock
    private CommonUtil commonUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Principal principal;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    private UserDtls user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserDtls();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
    }

    private void mockPrincipal() {
        when(principal.getName()).thenReturn("test@example.com");
    }

    @Test
    void testHome() {
        String view = userController.home();
        assertEquals("user/home", view);
    }

    @Test
    void testGetUserDetails() {
        mockPrincipal();
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(cartService.getCountCart(user.getId())).thenReturn(5);
        List<Category> categories = Arrays.asList(new Category());
        when(categoryService.getAllActiveCategory()).thenReturn(categories);

        userController.getUserDetails(principal, model);

        verify(model).addAttribute("user", user);
        verify(model).addAttribute("countCart", 5);
        verify(model).addAttribute("categories", categories);
    }

    @Test
    void testProfile() {
        String view = userController.profile();
        assertEquals("user/profile", view);
    }

    @Test
    void testLoadSuccess() {
        String view = userController.loadSuccess();
        assertEquals("user/success", view);
    }

    @Test
    void testUpdateProfile_Success() {
        MultipartFile img = mock(MultipartFile.class);
        when(userService.updateUserProfile(user, img)).thenReturn(user);

        String view = userController.updateProfile(user, img, redirectAttributes);

        assertEquals("redirect:/user/profile", view);
        verify(redirectAttributes).addFlashAttribute("succMsg", "Profile Updated");
    }

    @Test
    void testUpdateProfile_Failure() {
        MultipartFile img = mock(MultipartFile.class);
        when(userService.updateUserProfile(user, img)).thenReturn(null);

        String view = userController.updateProfile(user, img, redirectAttributes);

        assertEquals("redirect:/user/profile", view);
        verify(redirectAttributes).addFlashAttribute("errorMsg", "Profile not updated");
    }

    @Test
    void testChangePassword_Success() {
        mockPrincipal();
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches("currentPassword", user.getPassword())).thenReturn(true);
        when(userService.updateUser(user)).thenReturn(user);

        String view = userController.changePassword("newPassword", "currentPassword", principal, redirectAttributes);

        assertEquals("redirect:/user/profile", view);
        verify(passwordEncoder).encode("newPassword");
        verify(userService).updateUser(user);
        verify(redirectAttributes).addFlashAttribute("succMsg", "Password Updated successfully");
    }

    @Test
    void testChangePassword_CurrentPasswordIncorrect() {
        mockPrincipal();
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        String view = userController.changePassword("newPassword", "wrongPassword", principal, redirectAttributes);

        assertEquals("redirect:/user/profile", view);
        verify(redirectAttributes).addFlashAttribute("errorMsg", "Current Password incorrect");
    }

    @Test
    void testChangePassword_UpdateFailure() {
        mockPrincipal();
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches("currentPassword", user.getPassword())).thenReturn(true);
        when(userService.updateUser(user)).thenReturn(null);

        String view = userController.changePassword("newPassword", "currentPassword", principal, redirectAttributes);

        assertEquals("redirect:/user/profile", view);
        verify(redirectAttributes).addFlashAttribute("errorMsg", "Password not updated! Error on server");
    }
}
