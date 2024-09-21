package com.ecom.controller;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private CartService cartService;

    @Mock
    private ProductService productService;

    @Mock
    private CommonUtil commonUtil;

    @Mock
    private UserService userService;

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
        user = new UserDtls(); // Initialize a UserDtls object for testing
        user.setId(1);
        user.setEmail("test@example.com");
    }

    private void mockPrincipal() {
        when(principal.getName()).thenReturn("test@example.com");
    }

    @Test
    void testOrderPage() {
        mockPrincipal();
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        
        // Create a cart with a non-null totalOrderPrice
        Cart cart = new Cart();
        cart.setId(1);
        //cart.setUserId(user.getId());
        cart.setTotalOrderPrice(300.0); // Ensure this is set to a non-null value
        
        List<Cart> carts = Arrays.asList(cart);
        when(cartService.getCartsByUser(user.getId())).thenReturn(carts);

        String view = orderController.orderPage(principal, model);

        assertEquals("user/order", view);
        verify(model).addAttribute("carts", carts);
        verify(model).addAttribute("orderPrice", 300.0);
        verify(model).addAttribute("totalOrderPrice", 650.0); // 300 + 250 + 100
    }

    @Test
    void testOrderNow_ProductExists() {
        mockPrincipal();
        
        // Create a product with a valid discount price
        Product product = new Product();
        product.setId(1);
        //product.setName("Test Product");
        product.setDiscountPrice(80.0); // Set a non-null discount price
        
        when(productService.getProductById(1)).thenReturn(product);

        String view = orderController.orderNow(1, principal, model, redirectAttributes);

        assertEquals("user/order", view);
        verify(model).addAttribute("orderPrice", 80.0);
        verify(model).addAttribute("totalOrderPrice", 430.0); // 80 + 250 + 100
        verify(model).addAttribute("product", product);
    }

    @Test
    void testOrderNow_ProductDoesNotExist() {
        mockPrincipal();
        when(productService.getProductById(1)).thenReturn(null);

        String view = orderController.orderNow(1, principal, model, redirectAttributes);

        assertEquals("redirect:/product/1", view);
        verify(redirectAttributes).addFlashAttribute("errorMsg", "Invalid product");
    }

    @Test
    void testSaveOrder() throws Exception {
        mockPrincipal();
        OrderRequest orderRequest = new OrderRequest();
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);

        String view = orderController.saveOrder(orderRequest, principal, redirectAttributes);

        assertEquals("redirect:/order/success", view);
        verify(orderService).saveOrder(user.getId(), orderRequest);
    }

    @Test
    void testLoadSuccess() {
        String view = orderController.loadSuccess();
        assertEquals("user/success", view);
    }

    @Test
    void testMyOrder() {
        mockPrincipal();
        List<ProductOrder> orders = Arrays.asList(new ProductOrder());
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(orderService.getOrdersByUser(user.getId())).thenReturn(orders);

        String view = orderController.myOrder(model, principal);

        assertEquals("user/my_orders", view);
        verify(model).addAttribute("orders", orders);
    }

    @Test
    void testUpdateOrderStatus_Success() {
        when(principal.getName()).thenReturn("test@example.com");
        ProductOrder order = new ProductOrder();
        when(orderService.updateOrderStatus(1, "SHIPPED")).thenReturn(order);
        when(principal.getName()).thenReturn("test@example.com");

        String view = orderController.updateOrderStatus(1, 2, redirectAttributes);

        assertEquals("redirect:/order/user-orders", view);
        verify(redirectAttributes).addFlashAttribute("succMsg", "Status Updated");
    }

    @Test
    void testUpdateOrderStatus_Failure() {
        when(principal.getName()).thenReturn("test@example.com");
        when(orderService.updateOrderStatus(1, "INVALID")).thenReturn(null);

        String view = orderController.updateOrderStatus(1, 99, redirectAttributes);

        assertEquals("redirect:/order/user-orders", view);
        verify(redirectAttributes).addFlashAttribute("errorMsg", "Status not updated");
    }
}
