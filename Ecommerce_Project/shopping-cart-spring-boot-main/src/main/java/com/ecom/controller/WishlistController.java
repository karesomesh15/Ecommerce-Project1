package com.ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ecom.model.UserDtls;
import com.ecom.model.Wishlist;
import com.ecom.service.impl.WishlistService;
import com.ecom.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;
    
    @Autowired
    private UserService userService;

    // Get the logged-in user details using Principal
    private UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        return userService.getUserByEmail(email);
    }

    // Add to wishlist
    @GetMapping("/add")
    public String addToWishlist(@RequestParam Integer productId, Principal p, HttpSession session) {
        UserDtls user = getLoggedInUserDetails(p);
        Wishlist wishlist = wishlistService.addToWishlist(user, productId);
        
        // Clear any messages after redirect
        return "redirect:/wishlist/view";
    }

    // Remove from wishlist
    @GetMapping("/remove")
    public String removeFromWishlist(@RequestParam Integer productId, Principal p, HttpSession session) {
        UserDtls user = getLoggedInUserDetails(p);
        wishlistService.removeFromWishlist(user, productId);
        session.setAttribute("succMsg", "Product removed from wishlist");
        
        // Redirect to wishlist page
        return "redirect:/wishlist/view";
    }

    // View wishlist
    @GetMapping("/view")
    public String viewWishlist(Principal p, Model m) {
        UserDtls user = getLoggedInUserDetails(p);
        List<Wishlist> wishlist = wishlistService.getWishlistByUser(user);
        m.addAttribute("wishlist", wishlist);
        
        // Optionally clear any messages related to wishlist actions
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        session.removeAttribute("succMsg");
        session.removeAttribute("errorMsg");
        
        return "user/wishlist";
    }
}