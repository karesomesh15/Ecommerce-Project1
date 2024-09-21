package com.ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecom.model.Cart;
import com.ecom.model.OrderRequest;
import com.ecom.model.Product;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.service.CartService;
import com.ecom.service.OrderService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import com.ecom.util.CommonUtil;
import com.ecom.util.OrderStatus;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private UserService userService;

    private UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        return userService.getUserByEmail(email);
    }

    @GetMapping("/orders")
    public String orderPage(Principal p, Model m) {
        UserDtls user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);
        if (!carts.isEmpty()) {
            Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            Double totalOrderPrice = orderPrice + 250 + 100; // Example values for delivery and tax
            m.addAttribute("orderPrice", orderPrice);
            m.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        return "user/order";
    }

    @GetMapping("/orderNow")
    public String orderNow(@RequestParam Integer pid, Principal p, Model m, RedirectAttributes redirectAttributes) {
        if (p == null) {
            return "redirect:/signin";
        }

        Product product = productService.getProductById(pid);

        if (product == null) {
            redirectAttributes.addFlashAttribute("errorMsg", "Invalid product");
            return "redirect:/product/" + pid;
        }

        double deliveryFee = 250;
        double tax = 100;
        double totalPrice = product.getDiscountPrice();
        double totalOrderPrice = totalPrice + deliveryFee + tax;

        m.addAttribute("orderPrice", totalPrice);
        m.addAttribute("totalOrderPrice", totalOrderPrice);
        m.addAttribute("product", product);

        return "user/order";
    }

    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute OrderRequest request, Principal p, RedirectAttributes redirectAttributes) throws Exception {
        UserDtls user = getLoggedInUserDetails(p);
        orderService.saveOrder(user.getId(), request);
        return "redirect:/order/success";
    }

    @GetMapping("/success")
    public String loadSuccess() {
        return "user/success";
    }

    @GetMapping("/user-orders")
    public String myOrder(Model m, Principal p) {
        UserDtls loginUser = getLoggedInUserDetails(p);
        List<ProductOrder> orders = orderService.getOrdersByUser(loginUser.getId());
        m.addAttribute("orders", orders);
        return "user/my_orders";
    }

    @GetMapping("/update-status")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, RedirectAttributes redirectAttributes) {
        OrderStatus status = OrderStatus.fromId(st);
        if (status != null) {
            ProductOrder updateOrder = orderService.updateOrderStatus(id, status.getName());
            try {
                commonUtil.sendMailForProductOrder(updateOrder, status.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            redirectAttributes.addFlashAttribute("succMsg", "Status Updated");
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Status not updated");
        }
        return "redirect:/order/user-orders";
    }
}