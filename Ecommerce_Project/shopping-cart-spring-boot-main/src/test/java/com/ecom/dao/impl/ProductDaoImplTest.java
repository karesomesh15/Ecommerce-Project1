package com.ecom.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ecom.model.Product;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ProductDaoImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Product> typedQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    @InjectMocks
    private ProductDaoImpl productDaoImpl;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Arrange
        String jpql = "SELECT p FROM Product p";

        Product product1 = new Product();
        product1.setId((int) 1L);
        //product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId((int) 2L);
        //product2.setName("Product 2");

        List<Product> productList = Arrays.asList(product1, product2);

        // Simulate entityManager.createQuery() returning the typed query
        when(entityManager.createQuery(jpql, Product.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(productList);

        // Act
        List<Product> result = productDaoImpl.findAll();

        // Assert
        assertEquals(2, result.size());  // Verify that 2 products are returned
        //assertEquals("Product 1", result.get(0).getName());  // Verify the first product's name
        //assertEquals("Product 2", result.get(1).getName());  // Verify the second product's name

        // Verify that the entityManager and typedQuery were called correctly
        verify(entityManager).createQuery(jpql, Product.class);
        verify(typedQuery).getResultList();  // Verify that getResultList was called
    }

    @Test
    void testFindAll_Pageable() {
        // Arrange
        String jpql = "SELECT p FROM Product p";
        Pageable pageable = PageRequest.of(0, 2); // First page, 2 items per page

        Product product1 = new Product();
        product1.setId((int) 1L);
        //product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId((int) 2L);
        //product2.setName("Product 2");

        List<Product> productList = Arrays.asList(product1, product2);

        // Mock the behavior of the entityManager and typedQuery
        when(entityManager.createQuery(jpql, Product.class)).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(0)).thenReturn(typedQuery); // Offset = 0 for the first page
        when(typedQuery.setMaxResults(2)).thenReturn(typedQuery);  // Page size = 2
        when(typedQuery.getResultList()).thenReturn(productList);

        // Mock the behavior of the count query
        when(entityManager.createQuery("SELECT COUNT(p) FROM Product p", Long.class)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(10L); // Simulating a total of 10 products

        // Act
        Page<Product> result = productDaoImpl.findAll(pageable);

        // Assert
        assertEquals(2, result.getNumberOfElements()); // Verify 2 elements are returned on the first page
        assertEquals(10, result.getTotalElements());   // Verify total elements count is 10
        //assertEquals("Product 1", result.getContent().get(0).getName()); // Verify the first product's name
        //assertEquals("Product 2", result.getContent().get(1).getName()); // Verify the second product's name

        // Verify that the queries were called correctly
        verify(entityManager).createQuery(jpql, Product.class);
        verify(typedQuery).setFirstResult(0); // Verify pagination offset
        verify(typedQuery).setMaxResults(2);  // Verify pagination limit
        verify(typedQuery).getResultList();   // Verify result list retrieval
        verify(entityManager).createQuery("SELECT COUNT(p) FROM Product p", Long.class);
        verify(countQuery).getSingleResult(); // Verify total count retrieval
    }
    
    @Test
    void testFindByIsActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase() {
        // Arrange
        String searchTitle = "test";
        String searchCategory = "electronics";
        Pageable pageable = PageRequest.of(0, 2); // First page, 2 items per page
        
        Product product1 = new Product();
        product1.setId((int) 1L);
        product1.setTitle("Test Product 1");
        product1.setCategory("Electronics");

        Product product2 = new Product();
        product2.setId((int) 2L);
        product2.setTitle("Another Test");
        product2.setCategory("Home Appliances");

        List<Product> productList = Arrays.asList(product1, product2);

        // Mock the behavior of the entityManager and typedQuery
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("ch", searchTitle)).thenReturn(typedQuery);
        when(typedQuery.setParameter("ch2", searchCategory)).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(0)).thenReturn(typedQuery); // Offset = 0
        when(typedQuery.setMaxResults(2)).thenReturn(typedQuery); // Page size = 2
        when(typedQuery.getResultList()).thenReturn(productList);

        // Mock the count query
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter("ch", searchTitle)).thenReturn(countQuery);
        when(countQuery.setParameter("ch2", searchCategory)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(2L); // Total count of matching products

        // Act
        Page<Product> result = productDaoImpl.findByIsActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(searchTitle, searchCategory, pageable);

        // Assert
        assertEquals(2, result.getNumberOfElements()); // Verify 2 elements are returned
        assertEquals(2, result.getTotalElements());   // Verify total elements count is 2
        assertEquals("Test Product 1", result.getContent().get(0).getTitle()); // Verify the first product's title
        assertEquals("Another Test", result.getContent().get(1).getTitle()); // Verify the second product's title

        // Verify that the queries were called correctly
        verify(entityManager).createQuery(anyString(), eq(Product.class));
        verify(typedQuery).setParameter("ch", searchTitle);
        verify(typedQuery).setParameter("ch2", searchCategory);
        verify(typedQuery).setFirstResult(0);
        verify(typedQuery).setMaxResults(2);
        verify(typedQuery).getResultList();
        verify(entityManager).createQuery(anyString(), eq(Long.class));
        verify(countQuery).setParameter("ch", searchTitle);
        verify(countQuery).setParameter("ch2", searchCategory);
        verify(countQuery).getSingleResult();
    }
    
    @Test
    void testFindByIsActiveTrue() {
        // Arrange
        String jpql = "SELECT p FROM Product p WHERE p.isActive = true";

        Product product1 = new Product();
        product1.setId((int) 1L);
        product1.setTitle("Active Product 1");
        product1.setIsActive(true);

        Product product2 = new Product();
        product2.setId((int) 2L);
        product2.setTitle("Active Product 2");
        product2.setIsActive(true);

        List<Product> activeProducts = Arrays.asList(product1, product2);

        // Mock the behavior of the entityManager and typedQuery
        when(entityManager.createQuery(jpql, Product.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(activeProducts);

        // Act
        List<Product> result = productDaoImpl.findByIsActiveTrue();

        // Assert
        assertEquals(2, result.size());  // Verify that 2 active products are returned
        assertEquals("Active Product 1", result.get(0).getTitle());  // Verify the first product's title
        assertEquals("Active Product 2", result.get(1).getTitle());  // Verify the second product's title

        // Verify that the entityManager and typedQuery were called correctly
        verify(entityManager).createQuery(jpql, Product.class);
        verify(typedQuery).getResultList();  // Verify that getResultList was called
    }
    
    @Test
    void testFindByIsActiveTrue_Pageable() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 2); // First page, 2 items per page
        String jpql = "SELECT p FROM Product p WHERE p.isActive = true";

        Product product1 = new Product();
        product1.setId((int) 1L);
        product1.setTitle("Active Product 1");
        product1.setIsActive(true);

        Product product2 = new Product();
        product2.setId((int) 2L);
        product2.setTitle("Active Product 2");
        product2.setIsActive(true);

        List<Product> activeProducts = Arrays.asList(product1, product2);

        // Mock the behavior of the entityManager and typedQuery
        when(entityManager.createQuery(jpql, Product.class)).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(0)).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(2)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(activeProducts);

        // Mock the count query
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(2L); // Total count of active products

        // Act
        Page<Product> result = productDaoImpl.findByIsActiveTrue(pageable);

        // Assert
        assertEquals(2, result.getNumberOfElements()); // Verify that 2 active products are returned
        assertEquals(2, result.getTotalElements());   // Verify total elements count is 2
        assertEquals("Active Product 1", result.getContent().get(0).getTitle()); // Verify the first product's title
        assertEquals("Active Product 2", result.getContent().get(1).getTitle()); // Verify the second product's title

        // Verify that the queries were called correctly
        verify(entityManager).createQuery(jpql, Product.class);
        verify(typedQuery).setFirstResult(0);
        verify(typedQuery).setMaxResults(2);
        verify(typedQuery).getResultList();
        verify(entityManager).createQuery(anyString(), eq(Long.class));
        verify(countQuery).getSingleResult();
    }
    
    @Test
    void testFindByCategory() {
        // Arrange
        String category = "Electronics";
        String jpql = "SELECT p FROM Product p WHERE p.category = :category";

        Product product1 = new Product();
        product1.setId((int) 1L);
        product1.setTitle("Smartphone");
        product1.setCategory("Electronics");

        Product product2 = new Product();
        product2.setId((int) 2L);
        product2.setTitle("Laptop");
        product2.setCategory("Electronics");

        List<Product> products = Arrays.asList(product1, product2);

        // Mock the behavior of the entityManager and typedQuery
        when(entityManager.createQuery(jpql, Product.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("category", category)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(products);

        // Act
        List<Product> result = productDaoImpl.findByCategory(category);

        // Assert
        assertEquals(2, result.size());  // Verify that 2 products are returned
        assertEquals("Smartphone", result.get(0).getTitle());  // Verify the first product's title
        assertEquals("Laptop", result.get(1).getTitle());      // Verify the second product's title

        // Verify that the query was called correctly
        verify(entityManager).createQuery(jpql, Product.class);
        verify(typedQuery).setParameter("category", category);
        verify(typedQuery).getResultList();  // Verify that getResultList was called
    }
    
    @Test
    void testFindByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase() {
        // Arrange
        String titlePart = "smart";
        String categoryPart = "electronics";
        String jpql = "SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :ch, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%', :ch2, '%'))";

        Product product1 = new Product();
        product1.setId((int) 1L);
        product1.setTitle("Smartphone");
        product1.setCategory("Electronics");

        Product product2 = new Product();
        product2.setId((int) 2L);
        product2.setTitle("Tablet");
        product2.setCategory("Electronics");

        List<Product> products = Arrays.asList(product1, product2);

        // Mock the behavior of the entityManager and typedQuery
        when(entityManager.createQuery(jpql, Product.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("ch", titlePart)).thenReturn(typedQuery);
        when(typedQuery.setParameter("ch2", categoryPart)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(products);

        // Act
        List<Product> result = productDaoImpl.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(titlePart, categoryPart);

        // Assert
        assertEquals(2, result.size());  // Verify that 2 products are returned
        assertEquals("Smartphone", result.get(0).getTitle());  // Verify the first product's title
        assertEquals("Tablet", result.get(1).getTitle());      // Verify the second product's title

        // Verify that the query was called correctly
        verify(entityManager).createQuery(jpql, Product.class);
        verify(typedQuery).setParameter("ch", titlePart);
        verify(typedQuery).setParameter("ch2", categoryPart);
        verify(typedQuery).getResultList();  // Verify that getResultList was called
    }
    
    @Test
    void testFindByCategory_Pageable() {
        // Arrange
        String category = "Electronics";
        Pageable pageable = PageRequest.of(0, 2); // First page, 2 items per page
        String jpql = "SELECT p FROM Product p WHERE p.category = :category";

        Product product1 = new Product();
        product1.setId((int) 1L);
        product1.setTitle("Smartphone");
        product1.setCategory("Electronics");

        Product product2 = new Product();
        product2.setId((int) 2L);
        product2.setTitle("Laptop");
        product2.setCategory("Electronics");

        List<Product> products = Arrays.asList(product1, product2);

        // Mock the behavior of the entityManager and typedQuery
        when(entityManager.createQuery(jpql, Product.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("category", category)).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(0)).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(2)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(products);

        // Mock the count query
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter("category", category)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(2L); // Total count of products in this category

        // Act
        Page<Product> result = productDaoImpl.findByCategory(pageable, category);

        // Assert
        assertEquals(2, result.getNumberOfElements()); // Verify that 2 products are returned
        assertEquals(2, result.getTotalElements());   // Verify total elements count is 2
        assertEquals("Smartphone", result.getContent().get(0).getTitle()); // Verify the first product's title
        assertEquals("Laptop", result.getContent().get(1).getTitle());      // Verify the second product's title

        // Verify that the queries were called correctly
        verify(entityManager).createQuery(jpql, Product.class);
        verify(typedQuery).setParameter("category", category);
        verify(typedQuery).setFirstResult(0);
        verify(typedQuery).setMaxResults(2);
        verify(typedQuery).getResultList();
        verify(entityManager).createQuery(anyString(), eq(Long.class));
        verify(countQuery).setParameter("category", category);
        verify(countQuery).getSingleResult();
    }
    
    @Test
    void testFindByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase_Pageable() {
        // Arrange
        String titlePart = "smart";
        String categoryPart = "electronics";
        Pageable pageable = PageRequest.of(0, 2); // First page, 2 items per page
        String jpql = "SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :ch, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%', :ch2, '%'))";

        Product product1 = new Product();
        product1.setId((int) 1L);
        product1.setTitle("Smartphone");
        product1.setCategory("Electronics");

        Product product2 = new Product();
        product2.setId((int) 2L);
        product2.setTitle("Smart TV");
        product2.setCategory("Electronics");

        List<Product> products = Arrays.asList(product1, product2);

        // Mock the behavior of the entityManager and typedQuery
        when(entityManager.createQuery(jpql, Product.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("ch", titlePart)).thenReturn(typedQuery);
        when(typedQuery.setParameter("ch2", categoryPart)).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(0)).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(2)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(products);

        // Mock the count query
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter("ch", titlePart)).thenReturn(countQuery);
        when(countQuery.setParameter("ch2", categoryPart)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(2L); // Total count of products matching the criteria

        // Act
        Page<Product> result = productDaoImpl.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(titlePart, categoryPart, pageable);

        // Assert
        assertEquals(2, result.getNumberOfElements()); // Verify that 2 products are returned
        assertEquals(2, result.getTotalElements());   // Verify total elements count is 2
        assertEquals("Smartphone", result.getContent().get(0).getTitle()); // Verify the first product's title
        assertEquals("Smart TV", result.getContent().get(1).getTitle());      // Verify the second product's title

        // Verify that the queries were called correctly
        verify(entityManager).createQuery(jpql, Product.class);
        verify(typedQuery).setParameter("ch", titlePart);
        verify(typedQuery).setParameter("ch2", categoryPart);
        verify(typedQuery).setFirstResult(0);
        verify(typedQuery).setMaxResults(2);
        verify(typedQuery).getResultList();
        verify(entityManager).createQuery(anyString(), eq(Long.class));
        verify(countQuery).setParameter("ch", titlePart);
        verify(countQuery).setParameter("ch2", categoryPart);
        verify(countQuery).getSingleResult();
    }
    
    @Test
    void testFindById_Found() {
        // Arrange
        Integer productId = 1;
        Product expectedProduct = new Product();
        expectedProduct.setId(productId);
        expectedProduct.setTitle("Smartphone");
        expectedProduct.setCategory("Electronics");

        // Mock the behavior of the entity manager
        when(entityManager.find(Product.class, productId)).thenReturn(expectedProduct);

        // Act
        Optional<Product> result = productDaoImpl.findById(productId);

        // Assert
        assertEquals(expectedProduct, result.orElse(null)); // Verify the returned product matches the expected product
        verify(entityManager).find(Product.class, productId); // Verify that the find method was called with the correct parameters
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        Integer productId = 1;

        // Mock the behavior of the entity manager to return null
        when(entityManager.find(Product.class, productId)).thenReturn(null);

        // Act
        Optional<Product> result = productDaoImpl.findById(productId);

        // Assert
        assertEquals(Optional.empty(), result); // Verify that an empty Optional is returned
        verify(entityManager).find(Product.class, productId); // Verify that the find method was called with the correct parameters
    }
    
    @Test
    void testSave_NewProduct() {
        // Arrange
        Product newProduct = new Product();
        newProduct.setTitle("Smartphone");
        newProduct.setCategory("Electronics");
        
        // Mock the behavior of the entity manager
        //when(entityManager.persist(newProduct)).thenReturn(null);

        // Act
        Product savedProduct = productDaoImpl.save(newProduct);

        // Assert
        assertEquals(newProduct, savedProduct); // Verify that the returned product matches the input
        verify(entityManager).persist(newProduct); // Verify that persist was called
    }

    @Test
    void testSave_ExistingProduct() {
        // Arrange
        Product existingProduct = new Product();
        existingProduct.setId(1); // Assume the product has an ID
        existingProduct.setTitle("Smartphone");
        existingProduct.setCategory("Electronics");

        // Mock the behavior of the entity manager
        when(entityManager.merge(existingProduct)).thenReturn(existingProduct);

        // Act
        Product savedProduct = productDaoImpl.save(existingProduct);

        // Assert
        assertEquals(existingProduct, savedProduct); // Verify that the returned product matches the input
        verify(entityManager).merge(existingProduct); // Verify that merge was called
    }

    @Test
    void testDelete_ContainedProduct() {
        // Arrange
        Product product = new Product();
        product.setId(1);

        // Mock the behavior of the entity manager
        when(entityManager.contains(product)).thenReturn(true);

        // Act
        productDaoImpl.delete(product);

        // Assert
        verify(entityManager).remove(product); // Verify that remove was called
    }

    @Test
    void testDelete_NonContainedProduct() {
        // Arrange
        Product product = new Product();
        product.setId(1);

        // Mock the behavior of the entity manager
        when(entityManager.contains(product)).thenReturn(false);
        when(entityManager.merge(product)).thenReturn(product);

        // Act
        productDaoImpl.delete(product);

        // Assert
        verify(entityManager).remove(product); // Verify that remove was called
        verify(entityManager).merge(product); // Verify that merge was called
    }
}
