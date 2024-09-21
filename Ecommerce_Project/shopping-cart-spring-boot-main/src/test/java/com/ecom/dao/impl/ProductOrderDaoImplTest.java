package com.ecom.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ecom.model.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ProductOrderDaoImplTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ProductOrderDaoImpl productOrderDaoImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUserId() {
        // Arrange
        Integer userId = 1;
        List<ProductOrder> expectedOrders = new ArrayList<>();
        ProductOrder order = new ProductOrder();
        expectedOrders.add(order);

        TypedQuery<ProductOrder> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(ProductOrder.class))).thenReturn(query);
        when(query.setParameter("userId", userId)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedOrders);

        // Act
        List<ProductOrder> actualOrders = productOrderDaoImpl.findByUserId(userId);

        // Assert
        assertEquals(expectedOrders, actualOrders);
        verify(entityManager).createQuery(anyString(), eq(ProductOrder.class));
        verify(query).setParameter("userId", userId);
    }

    @Test
    void testFindByOrderId_Found() {
        // Arrange
        String orderId = "ORDER123";
        ProductOrder expectedOrder = new ProductOrder();
        expectedOrder.setOrderId(orderId);

        TypedQuery<ProductOrder> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(ProductOrder.class))).thenReturn(query);
        when(query.setParameter("orderId", orderId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(expectedOrder));

        // Act
        Optional<ProductOrder> actualOrder = productOrderDaoImpl.findByOrderId(orderId);

        // Assert
        assertTrue(actualOrder.isPresent());
        assertEquals(expectedOrder, actualOrder.get());
    }

    @Test
    void testFindByOrderId_NotFound() {
        // Arrange
        String orderId = "ORDER123";

        TypedQuery<ProductOrder> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(ProductOrder.class))).thenReturn(query);
        when(query.setParameter("orderId", orderId)).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());

        // Act
        Optional<ProductOrder> actualOrder = productOrderDaoImpl.findByOrderId(orderId);

        // Assert
        assertFalse(actualOrder.isPresent());
    }

    @Test
    void testSave_NewProductOrder() {
        // Arrange
        ProductOrder newOrder = new ProductOrder();
        newOrder.setOrderId("ORDER123");

        // Act
        ProductOrder savedOrder = productOrderDaoImpl.save(newOrder);

        // Assert
        assertEquals(newOrder, savedOrder);
        verify(entityManager).persist(newOrder);
    }

    @Test
    void testSave_ExistingProductOrder() {
        // Arrange
        ProductOrder existingOrder = new ProductOrder();
        existingOrder.setId(1);
        existingOrder.setOrderId("ORDER123");

        // Mock merge behavior
        when(entityManager.merge(existingOrder)).thenReturn(existingOrder);

        // Act
        ProductOrder savedOrder = productOrderDaoImpl.save(existingOrder);

        // Assert
        assertEquals(existingOrder, savedOrder);
        verify(entityManager).merge(existingOrder);
    }

    @Test
    void testDelete_ContainedProductOrder() {
        // Arrange
        ProductOrder order = new ProductOrder();
        order.setId(1);

        when(entityManager.contains(order)).thenReturn(true);

        // Act
        productOrderDaoImpl.delete(order);

        // Assert
        verify(entityManager).remove(order);
    }

    @Test
    void testDelete_NonContainedProductOrder() {
        // Arrange
        ProductOrder order = new ProductOrder();
        order.setId(1);

        when(entityManager.contains(order)).thenReturn(false);
        when(entityManager.merge(order)).thenReturn(order);

        // Act
        productOrderDaoImpl.delete(order);

        // Assert
        verify(entityManager).remove(order);
        verify(entityManager).merge(order);
    }

    @Test
    void testFindById_Found() {
        // Arrange
        Integer orderId = 1;
        ProductOrder expectedOrder = new ProductOrder();
        expectedOrder.setId(orderId);

        when(entityManager.find(ProductOrder.class, orderId)).thenReturn(expectedOrder);

        // Act
        Optional<ProductOrder> actualOrder = productOrderDaoImpl.findById(orderId);

        // Assert
        assertTrue(actualOrder.isPresent());
        assertEquals(expectedOrder, actualOrder.get());
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        Integer orderId = 1;

        when(entityManager.find(ProductOrder.class, orderId)).thenReturn(null);

        // Act
        Optional<ProductOrder> actualOrder = productOrderDaoImpl.findById(orderId);

        // Assert
        assertFalse(actualOrder.isPresent());
    }

    @Test
    void testFindAll() {
        // Arrange
        List<ProductOrder> expectedOrders = new ArrayList<>();
        expectedOrders.add(new ProductOrder());

        TypedQuery<ProductOrder> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(ProductOrder.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedOrders);

        // Act
        List<ProductOrder> actualOrders = productOrderDaoImpl.findAll();

        // Assert
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void testFindAll_Pageable() {
        // Arrange
        Pageable pageable = Pageable.ofSize(10);
        List<ProductOrder> expectedOrders = new ArrayList<>();
        expectedOrders.add(new ProductOrder());

        TypedQuery<ProductOrder> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(ProductOrder.class))).thenReturn(query);
        when(query.setFirstResult(anyInt())).thenReturn(query);
        when(query.setMaxResults(anyInt())).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedOrders);

        TypedQuery<Long> countQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        // Act
        Page<ProductOrder> actualPage = productOrderDaoImpl.findAll(pageable);

        // Assert
        assertEquals(expectedOrders, actualPage.getContent());
        assertEquals(1L, actualPage.getTotalElements());
    }
}
