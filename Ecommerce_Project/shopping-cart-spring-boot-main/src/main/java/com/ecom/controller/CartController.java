package com.ecom.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecom.model.Cart;
import com.ecom.model.UserDtls;
import com.ecom.service.CartService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    private UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        return userService.getUserByEmail(email);
    }

    @GetMapping("/add")
    public String addToCart(@RequestParam Integer pid, @RequestParam Integer quantity, Principal p, RedirectAttributes redirectAttributes) {
        UserDtls user = getLoggedInUserDetails(p);
        Cart saveCart = cartService.saveCart(pid, user.getId(), quantity);
        if (ObjectUtils.isEmpty(saveCart)) {
            redirectAttributes.addFlashAttribute("errorMsg", "Product added to cart failed");
        } else {
            redirectAttributes.addFlashAttribute("succMsg", "Product added to cart");
        }
        return "redirect:/product/" + pid;
    }

    @GetMapping("/view")
    public String loadCartPage(Principal p, Model m) {
        UserDtls user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);
        if (!carts.isEmpty()) {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            m.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        return "user/cart";
    }

    @GetMapping("/update-quantity")
    public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer cid, RedirectAttributes redirectAttributes) {
        cartService.updateQuantity(sy, cid);
        return "redirect:/cart/view";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam Map<String, String> params, Principal principal, RedirectAttributes redirectAttributes) {
        String userId = principal.getName();

        // Check if the request is for removing an item
        if (params.containsKey("remove")) {
            try {
                int cartId = Integer.parseInt(params.get("remove"));
                cartService.removeFromCart(cartId);
                redirectAttributes.addFlashAttribute("succMsg", "Item removed from cart");
            } catch (NumberFormatException e) {
                redirectAttributes.addFlashAttribute("errorMsg", "Invalid cart ID for removal");
            }
        } else {
            // Otherwise, process the quantity update
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (key.startsWith("quantity[")) {
                    try {
                        String cartIdStr = key.substring(10, key.length() - 1);
                        int cartId = Integer.parseInt(cartIdStr);

                        if (value != null && !value.isEmpty()) {
                            int quantity = Integer.parseInt(value);
                            if (quantity > 0) {
                                cartService.updateQuantity(cartId, quantity);
                            } else {
                                redirectAttributes.addFlashAttribute("errorMsg", "Quantity must be greater than zero");
                                return "redirect:/cart/view";
                            }
                        } else {
                            redirectAttributes.addFlashAttribute("errorMsg", "Quantity value is missing");
                            return "redirect:/cart/view";
                        }
                    } catch (NumberFormatException e) {
                        redirectAttributes.addFlashAttribute("errorMsg", "Invalid cart ID or quantity value");
                        return "redirect:/cart/view";
                    }
                }
            }
        }

        redirectAttributes.addFlashAttribute("succMsg", "Cart updated successfully");
        return "redirect:/cart/view";
    }
}