package com.ecom.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.ecom.dao.ProductOrderDao;
import com.ecom.model.Cart;
import com.ecom.model.OrderAddress;
import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;
import com.ecom.repository.CartRepository;
import com.ecom.util.CommonUtil;

class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private ProductOrderDao orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CommonUtil commonUtil;

    private Cart cart;
    private ProductOrder productOrder;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up mock Cart
        cart = new Cart();
        // Assume Product and User are set properly
        cart.setQuantity(2);
        // Set Product, User, etc. as needed

        // Set up mock ProductOrder
        productOrder = new ProductOrder();
        productOrder.setOrderId(UUID.randomUUID().toString());
        productOrder.setOrderDate(LocalDate.now());
        productOrder.setQuantity(cart.getQuantity());
        // Set other fields as needed

        // Set up OrderRequest
        orderRequest = new OrderRequest();
        orderRequest.setFirstName("John");
        orderRequest.setLastName("Doe");
        orderRequest.setEmail("john.doe@example.com");
        orderRequest.setMobileNo("1234567890");
        orderRequest.setAddress("123 Main St");
        orderRequest.setCity("City");
        orderRequest.setState("State");
        orderRequest.setPincode("123456");
        orderRequest.setPaymentType("CREDIT_CARD");
    }

	/*
	 * @Test void testSaveOrder() throws Exception {
	 * when(cartRepository.findByUserId(1)).thenReturn(Arrays.asList(cart));
	 * when(orderRepository.save(any(ProductOrder.class))).thenReturn(productOrder);
	 * doNothing().when(commonUtil).sendMailForProductOrder(any(ProductOrder.class),
	 * anyString());
	 * 
	 * orderService.saveOrder(1, orderRequest);
	 * 
	 * verify(orderRepository, times(1)).save(any(ProductOrder.class));
	 * verify(commonUtil, times(1)).sendMailForProductOrder(any(ProductOrder.class),
	 * eq("success")); }
	 */
    
    @Test
    void testGetOrdersByUser() {
        when(orderRepository.findByUserId(1)).thenReturn(Arrays.asList(productOrder));

        List<ProductOrder> orders = orderService.getOrdersByUser(1);

        assertEquals(1, orders.size());
        assertEquals(productOrder, orders.get(0));
    }

    @Test
    void testUpdateOrderStatus_Success() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(productOrder));
        when(orderRepository.save(productOrder)).thenReturn(productOrder);

        ProductOrder updatedOrder = orderService.updateOrderStatus(1, "COMPLETED");

        assertNotNull(updatedOrder);
        assertEquals("COMPLETED", updatedOrder.getStatus());
        verify(orderRepository).save(productOrder);
    }

    @Test
    void testUpdateOrderStatus_NotFound() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        ProductOrder updatedOrder = orderService.updateOrderStatus(1, "COMPLETED");

        assertNull(updatedOrder);
        verify(orderRepository, never()).save(any(ProductOrder.class));
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(productOrder));

        List<ProductOrder> orders = orderService.getAllOrders();

        assertEquals(1, orders.size());
        assertEquals(productOrder, orders.get(0));
    }

    @Test
    void testGetAllOrdersPagination() {
        Page<ProductOrder> page = new PageImpl<>(Arrays.asList(productOrder));
        when(orderRepository.findAll(PageRequest.of(0, 1))).thenReturn(page);

        Page<ProductOrder> resultPage = orderService.getAllOrdersPagination(0, 1);

        assertEquals(1, resultPage.getContent().size());
        assertEquals(productOrder, resultPage.getContent().get(0));
    }

    @Test
    void testGetOrdersByOrderId_Found() {
        when(orderRepository.findByOrderId(anyString())).thenReturn(Optional.of(productOrder));

        ProductOrder foundOrder = orderService.getOrdersByOrderId(productOrder.getOrderId());

        assertNotNull(foundOrder);
        assertEquals(productOrder.getOrderId(), foundOrder.getOrderId());
    }

    @Test
    void testGetOrdersByOrderId_NotFound() {
        when(orderRepository.findByOrderId(anyString())).thenReturn(Optional.empty());

        ProductOrder foundOrder = orderService.getOrdersByOrderId("nonexistent");

        assertNull(foundOrder);
    }
}
