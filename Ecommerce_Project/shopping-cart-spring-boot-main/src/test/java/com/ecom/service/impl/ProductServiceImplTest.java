package com.ecom.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import com.ecom.dao.ProductDao;
import com.ecom.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductDao productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1);
        product.setTitle("Test Product");
        product.setPrice(100.0);
        product.setDiscount((int) 10.0);
    }

    @Test
    void testSaveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product savedProduct = productService.saveProduct(product);
        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getTitle());
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));
        assertEquals(1, productService.getAllProducts().size());
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        Product foundProduct = productService.getProductById(1);
        assertNotNull(foundProduct);
        assertEquals("Test Product", foundProduct.getTitle());
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        assertTrue(productService.deleteProduct(1));
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testUpdateProduct() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        
        product.setTitle("Updated Product");
        Product updatedProduct = productService.updateProduct(product, mockFile);
        
        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getTitle());
        verify(productRepository, times(1)).save(product);
    }
}
