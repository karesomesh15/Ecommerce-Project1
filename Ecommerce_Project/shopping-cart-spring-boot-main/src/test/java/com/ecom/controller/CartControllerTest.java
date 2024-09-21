package com.ecom.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecom.model.Cart;
import com.ecom.model.UserDtls;
import com.ecom.service.CartService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;

class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartController cartController;

    @Mock
    private Principal principal;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddToCartSuccess() {
        when(principal.getName()).thenReturn("user@example.com");
        UserDtls user = new UserDtls();
        user.setId(1);
        when(userService.getUserByEmail("user@example.com")).thenReturn(user);
        when(cartService.saveCart(anyInt(), anyInt(), anyInt())).thenReturn(new Cart());

        String result = cartController.addToCart(1, 2, principal, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("succMsg", "Product added to cart");
        assertEquals("redirect:/product/1", result);
    }

    @Test
    void testAddToCartFailure() {
        when(principal.getName()).thenReturn("user@example.com");
        UserDtls user = new UserDtls();
        user.setId(1);
        when(userService.getUserByEmail("user@example.com")).thenReturn(user);
        when(cartService.saveCart(anyInt(), anyInt(), anyInt())).thenReturn(null);

        String result = cartController.addToCart(1, 2, principal, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("errorMsg", "Product added to cart failed");
        assertEquals("redirect:/product/1", result);
    }

    @Test
    void testLoadCartPageWithItems() {
        when(principal.getName()).thenReturn("user@example.com");
        UserDtls user = new UserDtls();
        user.setId(1);
        when(userService.getUserByEmail("user@example.com")).thenReturn(user);

        Cart cart1 = new Cart();
        Cart cart2 = new Cart();
        cart2.setTotalOrderPrice(200.0);
        List<Cart> carts = Arrays.asList(cart1, cart2);

        when(cartService.getCartsByUser(user.getId())).thenReturn(carts);

        String result = cartController.loadCartPage(principal, model);

        verify(model).addAttribute("carts", carts);
        verify(model).addAttribute("totalOrderPrice", 200.0);
        assertEquals("user/cart", result);
    }

    @Test
    void testLoadCartPageWithoutItems() {
        when(principal.getName()).thenReturn("user@example.com");
        UserDtls user = new UserDtls();
        user.setId(1);
        when(userService.getUserByEmail("user@example.com")).thenReturn(user);

        List<Cart> carts = Arrays.asList();

        when(cartService.getCartsByUser(user.getId())).thenReturn(carts);

        String result = cartController.loadCartPage(principal, model);

        verify(model).addAttribute("carts", carts);
        verify(model, never()).addAttribute(eq("totalOrderPrice"), any());
        assertEquals("user/cart", result);
    }

    @Test
    void testUpdateCartQuantity() {
        String result = cartController.updateCartQuantity("increase", 1, redirectAttributes);

        verify(cartService).updateQuantity("increase", 1);
        assertEquals("redirect:/cart/view", result);
    }

    @Test
    void testUpdateCartSuccess() {
        Map<String, String> params = new HashMap<>();
        params.put("quantity[1]", "3");
        when(principal.getName()).thenReturn("user@example.com");

        String result = cartController.updateCart(params, principal, redirectAttributes);

        verify(cartService).updateQuantity(1, 3);
        verify(redirectAttributes).addFlashAttribute("succMsg", "Cart updated successfully");
        assertEquals("redirect:/cart/view", result);
    }

    @Test
    void testUpdateCartInvalidQuantity() {
        Map<String, String> params = new HashMap<>();
        params.put("quantity[1]", "0");
        when(principal.getName()).thenReturn("user@example.com");

        String result = cartController.updateCart(params, principal, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("errorMsg", "Quantity must be greater than zero");
        assertEquals("redirect:/cart/view", result);
    }

    @Test
    void testUpdateCartInvalidCartId() {
        Map<String, String> params = new HashMap<>();
        params.put("quantity[abc]", "3"); // Invalid cart ID
        when(principal.getName()).thenReturn("user@example.com");

        String result = cartController.updateCart(params, principal, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("errorMsg", "Invalid cart ID or quantity value");
        assertEquals("redirect:/cart/view", result);
    }

    @Test
    void testRemoveFromCartSuccess() {
        Map<String, String> params = new HashMap<>();
        params.put("remove", "1");
        when(principal.getName()).thenReturn("user@example.com");

        String result = cartController.updateCart(params, principal, redirectAttributes);

        verify(cartService).removeFromCart(1);
        verify(redirectAttributes).addFlashAttribute("succMsg", "Item removed from cart");
        assertEquals("redirect:/cart/view", result);
    }

    @Test
    void testRemoveFromCartInvalidCartId() {
        Map<String, String> params = new HashMap<>();
        params.put("remove", "abc"); // Invalid cart ID
        when(principal.getName()).thenReturn("user@example.com");

        String result = cartController.updateCart(params, principal, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("errorMsg", "Invalid cart ID for removal");
        assertEquals("redirect:/cart/view", result);
    }
}
