package com.ecom.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ecom.model.UserDtls;
import com.ecom.model.Wishlist;
import com.ecom.service.impl.WishlistService;
import com.ecom.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

class WishlistControllerTest {

    @Mock
    private WishlistService wishlistService;

    @Mock
    private UserService userService;

    @Mock
    private Principal principal;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private WishlistController wishlistController;

    private UserDtls user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up common test data
        user = new UserDtls();
        user.setEmail("test@example.com");
        
        // Set up mock behavior for getting logged-in user's email
        when(principal.getName()).thenReturn("test@example.com");
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
    }

    @Test
    void testAddToWishlist() {
        Wishlist wishlist = new Wishlist();
        when(wishlistService.addToWishlist(eq(user), any(Integer.class))).thenReturn(wishlist);

        String view = wishlistController.addToWishlist(1, principal, session);

        verify(wishlistService, times(1)).addToWishlist(user, 1);
        assert (view.equals("redirect:/wishlist/view"));
    }

    @Test
    void testRemoveFromWishlist() {
        String view = wishlistController.removeFromWishlist(1, principal, session);

        verify(wishlistService, times(1)).removeFromWishlist(user, 1);
        verify(session, times(1)).setAttribute("succMsg", "Product removed from wishlist");
        assert (view.equals("redirect:/wishlist/view"));
    }

    @Test
    void testViewWishlist() {
        Wishlist wishlist1 = new Wishlist();
        Wishlist wishlist2 = new Wishlist();
        List<Wishlist> wishlist = List.of(wishlist1, wishlist2);
        when(wishlistService.getWishlistByUser(user)).thenReturn(wishlist);

        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        when(request.getSession()).thenReturn(session);

        String view = wishlistController.viewWishlist(principal, model);

        verify(wishlistService, times(1)).getWishlistByUser(user);
        verify(model, times(1)).addAttribute("wishlist", wishlist);
        verify(session, times(1)).removeAttribute("succMsg");
        verify(session, times(1)).removeAttribute("errorMsg");
        assert (view.equals("user/wishlist"));
    }

    // Additional Test Cases

    @Test
    void testAddToWishlistWithNullProductId() {
        String view = wishlistController.addToWishlist(null, principal, session);

        // Verify no service call is made since the productId is null
        verify(wishlistService, times(0)).addToWishlist(any(UserDtls.class), any(Integer.class));
        
        // Should still redirect to the wishlist view
        assert (view.equals("redirect:/wishlist/view"));
    }

    @Test
    void testRemoveFromWishlistWithNonExistentProductId() {
        // Simulate the removal of a product that doesn’t exist
        doNothing().when(wishlistService).removeFromWishlist(user, 999);

        String view = wishlistController.removeFromWishlist(999, principal, session);

        // Verify the service is called but no exception should be thrown
        verify(wishlistService, times(1)).removeFromWishlist(user, 999);
        verify(session, times(1)).setAttribute("succMsg", "Product removed from wishlist");
        assert (view.equals("redirect:/wishlist/view"));
    }

    @Test
    void testViewWishlistWhenEmpty() {
        when(wishlistService.getWishlistByUser(user)).thenReturn(Collections.emptyList());

        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        when(request.getSession()).thenReturn(session);

        String view = wishlistController.viewWishlist(principal, model);

        // Verify that an empty wishlist is passed to the model
        verify(model, times(1)).addAttribute("wishlist", Collections.emptyList());

        // Verify the view name
        assert (view.equals("user/wishlist"));
    }

    @Test
    void testAddToWishlistWhenUserNotFound() {
        // Simulate a case where the user is not found
        when(userService.getUserByEmail("test@example.com")).thenReturn(null);

        String view = wishlistController.addToWishlist(1, principal, session);

        // No service method should be invoked since the user is null
        verify(wishlistService, times(0)).addToWishlist(any(), any(Integer.class));

        // Check the view redirection still happens
        assert (view.equals("redirect:/wishlist/view"));
    }

	/*
	 * @Test void testViewWishlistWhenPrincipalIsNull() { // Simulate a case where
	 * the principal is null (user not logged in) String view =
	 * wishlistController.viewWishlist(null, model);
	 * 
	 * // Ensure no service calls are made verify(wishlistService,
	 * times(0)).getWishlistByUser(any());
	 * 
	 * // Should still return the wishlist view page, but no user data assert
	 * (view.equals("user/wishlist")); }
	 */
}
