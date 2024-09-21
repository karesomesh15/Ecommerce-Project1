package com.ecom.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ecom.dao.ProductDao;
import com.ecom.dao.UserDao;
import com.ecom.model.Cart;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.repository.CartRepository;

class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserDao userRepository;

    @Mock
    private ProductDao productRepository;

    private UserDtls user;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        user = new UserDtls();
        user.setId(1);
        user.setEmail("user@example.com");

        product = new Product();
        product.setId(1);
        product.setDiscountPrice(100.0);
    }

    @Test
    void testSaveCart_NewCart() {
        Integer productId = 1;
        Integer userId = 1;
        Integer quantity = 2;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByProductIdAndUserId(productId, userId)).thenReturn(null);

        Cart savedCartMock = new Cart();
        savedCartMock.setProduct(product);
        savedCartMock.setUser(user);
        savedCartMock.setQuantity(quantity);
        savedCartMock.setTotalPrice(quantity * product.getDiscountPrice());

        when(cartRepository.save(any(Cart.class))).thenReturn(savedCartMock);

        Cart savedCart = cartService.saveCart(productId, userId, quantity);

        assertNotNull(savedCart);
        assertEquals(product, savedCart.getProduct());
        assertEquals(user, savedCart.getUser());
        assertEquals(quantity, savedCart.getQuantity());
        assertEquals(quantity * product.getDiscountPrice(), savedCart.getTotalPrice());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testSaveCart_UpdateExistingCart() {
        Integer productId = 1;
        Integer userId = 1;
        Integer quantity = 1;

        Cart existingCart = new Cart();
        existingCart.setProduct(product);
        existingCart.setUser(user);
        existingCart.setQuantity(1);
        existingCart.setTotalPrice(1 * product.getDiscountPrice());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByProductIdAndUserId(productId, userId)).thenReturn(existingCart);

        Cart updatedCartMock = new Cart();
        updatedCartMock.setProduct(product);
        updatedCartMock.setUser(user);
        updatedCartMock.setQuantity(2); // Updated quantity
        updatedCartMock.setTotalPrice(2 * product.getDiscountPrice());

        when(cartRepository.save(any(Cart.class))).thenReturn(updatedCartMock);

        Cart updatedCart = cartService.saveCart(productId, userId, quantity);

        assertNotNull(updatedCart);
        assertEquals(2, updatedCart.getQuantity());
        assertEquals(2 * product.getDiscountPrice(), updatedCart.getTotalPrice());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testUpdateQuantity() {
        Integer cartId = 1;
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setQuantity(5);
        cart.setProduct(product);
        cart.setTotalPrice(500.0); // 5 * 100

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.updateQuantity(cartId, 3);

        assertEquals(3, cart.getQuantity());
        assertEquals(300.0, cart.getTotalPrice());
        verify(cartRepository).save(cart);
    }

    @Test
    void testUpdateQuantity_RemoveCart() {
        Integer cartId = 1;
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setQuantity(1);
        cart.setProduct(product);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        cartService.updateQuantity(cartId, 0);

        verify(cartRepository).delete(cart);
    }

    @Test
    void testRemoveFromCart() {
        Integer cartId = 1;
        Cart cart = new Cart();
        cart.setId(cartId);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        cartService.removeFromCart(cartId);

        verify(cartRepository).delete(cart);
    }

    @Test
    void testGetCartsByUser() {
        Integer userId = 1;
        Cart cart1 = new Cart();
        cart1.setProduct(product);
        cart1.setQuantity(2);
        Cart cart2 = new Cart();
        cart2.setProduct(product);
        cart2.setQuantity(3);

        when(cartRepository.findByUserId(userId)).thenReturn(Arrays.asList(cart1, cart2));

        List<Cart> carts = cartService.getCartsByUser(userId);

        assertEquals(2, carts.size());
        assertEquals(200.0, carts.get(0).getTotalPrice());
        assertEquals(300.0, carts.get(1).getTotalPrice());
    }

    @Test
    void testGetCountCart() {
        Integer userId = 1;
        when(cartRepository.countByUserId(userId)).thenReturn(3);

        Integer count = cartService.getCountCart(userId);

        assertEquals(3, count);
    }
}
