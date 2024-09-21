package com.ecom.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// import java.awt.print.Pageable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.springframework.data.domain.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.ecom.model.Category;

class CategoryDaoImplTest {

    @Mock
    private EntityManager entityManager;
    

    @Mock
    private TypedQuery<Long> typedQuery;
    
    @Mock
    private TypedQuery<Category> typedQuery2; 
    
    @Mock
    private TypedQuery<Category> typedQuery3;
    
    @Mock
    private TypedQuery<Category> typedQuery4;
    
    @Mock
    private TypedQuery<Long> countQuery;

    @InjectMocks
    private CategoryDaoImpl categoryDaoImpl;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExistsByName_Found() {
        // Arrange
        String categoryName = "Electronics";
        
        // Use eq(Long.class) to match the Class<Long> argument in the createQuery method
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("name", categoryName)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(1L); // Simulating one match

        // Act
        Boolean result = categoryDaoImpl.existsByName(categoryName);

        // Assert
        assertTrue(result); // Since 1 > 0, we expect the category to exist
    }

    @Test
    void testExistsByName_NotFound() {
        // Arrange
        String categoryName = "NonExistentCategory";

        // Again, use eq(Long.class) for the second argument
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("name", categoryName)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(0L); // Simulating no matches

        // Act
        Boolean result = categoryDaoImpl.existsByName(categoryName);

        // Assert
        assertFalse(result); // Since 0 <= 0, we expect the category to not exist
    }
    
    @Test
    void testFindByIsActiveTrue() {
        // Arrange
        Category category1 = new Category();
        category1.setId((int) 1L);
        category1.setName("Electronics");
        category1.setIsActive(true);

        Category category2 = new Category();
        category2.setId((int) 2L);
        category2.setName("Books");
        category2.setIsActive(true);

        List<Category> activeCategories = Arrays.asList(category1, category2);

        // Mock the behavior of entityManager and TypedQuery
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(typedQuery2);  // Ensure Category.class is used
        when(typedQuery2.getResultList()).thenReturn(activeCategories);  // Return the list of categories

        // Act
        List<Category> result = categoryDaoImpl.findByIsActiveTrue();

        // Assert
        assertEquals(2, result.size());  // Ensure the list contains 2 active categories
        assertEquals("Electronics", result.get(0).getName());
        assertEquals("Books", result.get(1).getName());
    }
    
    @Test
    void testSave_NewCategory_Persist() {
        // Arrange
        Category category = new Category();
        category.setName("New Category");
        category.setIsActive(true);  // Simulate new category, no ID set (null)

        // Act
        Category result = categoryDaoImpl.save(category);

        // Assert
        // Ensure persist is called when ID is null (new category)
        verify(entityManager).persist(category);
        verify(entityManager, never()).merge(category);  // merge should not be called
        assertEquals(category, result);  // Check the returned category is the same
    }

    @Test
    void testSave_ExistingCategory_Merge() {
        // Arrange
        Category category = new Category();
        category.setId((int) 1L);  // Simulate existing category with non-null ID
        category.setName("Existing Category");
        category.setIsActive(true);

        // Act
        Category result = categoryDaoImpl.save(category);

        // Assert
        // Ensure merge is called when ID is not null (existing category)
        verify(entityManager).merge(category);
        verify(entityManager, never()).persist(category);  // persist should not be called
        assertEquals(category, result);  // Check the returned category is the same
    }
    
    @Test
    void testDelete_CategoryManagedByEntityManager() {
        // Arrange
        Category category = new Category();
        category.setId((int) 1L);
        category.setName("Electronics");

        // Simulate the category is managed (entityManager.contains returns true)
        when(entityManager.contains(category)).thenReturn(true);

        // Act
        categoryDaoImpl.delete(category);

        // Assert
        verify(entityManager).contains(category);  // Verify contains() was called
        verify(entityManager).remove(category);    // Verify remove() was called directly
        verify(entityManager, never()).merge(category);  // Verify merge() was never called
    }

    @Test
    void testDelete_CategoryNotManagedByEntityManager() {
        // Arrange
        Category category = new Category();
        category.setId((int) 2L);
        category.setName("Books");

        // Simulate the category is not managed (entityManager.contains returns false)
        when(entityManager.contains(category)).thenReturn(false);

        // Mock the behavior of merging
        Category mergedCategory = new Category();
        mergedCategory.setId((int) 2L);
        mergedCategory.setName("Books");
        when(entityManager.merge(category)).thenReturn(mergedCategory);

        // Act
        categoryDaoImpl.delete(category);

        // Assert
        verify(entityManager).contains(category);  // Verify contains() was called
        verify(entityManager).merge(category);     // Verify merge() was called
        verify(entityManager).remove(mergedCategory); // Verify remove() was called with the merged entity
    }
    
    @Test
    void testFindById_CategoryFound() {
        // Arrange
        Integer categoryId = 1;
        Category category = new Category();
        category.setId((int) 1L);
        category.setName("Electronics");

        // Simulate entityManager.find() returning a valid category
        when(entityManager.find(eq(Category.class), eq(categoryId))).thenReturn(category);

        // Act
        Optional<Category> result = categoryDaoImpl.findById(categoryId);

        // Assert
        assertTrue(result.isPresent());  // Check that the Optional contains a value
        assertEquals(category, result.get());  // Ensure the returned category is the one we expect
    }

    @Test
    void testFindById_CategoryNotFound() {
        // Arrange
        Integer categoryId = 1;

        // Simulate entityManager.find() returning null (category not found)
        when(entityManager.find(eq(Category.class), eq(categoryId))).thenReturn(null);

        // Act
        Optional<Category> result = categoryDaoImpl.findById(categoryId);

        // Assert
        assertTrue(result.isEmpty());  // Check that the Optional is empty
    }
    
    @Test
    void testFindAll() {
        // Arrange
        String jpql = "SELECT c FROM Category c";
        Category category1 = new Category();
        category1.setId((int) 1L);
        category1.setName("Electronics");

        Category category2 = new Category();
        category2.setId((int) 2L);
        category2.setName("Books");

        List<Category> categoryList = Arrays.asList(category1, category2);

        // Simulate entityManager.createQuery() returning the typed query
        when(entityManager.createQuery(jpql, Category.class)).thenReturn(typedQuery3);

        // Simulate typedQuery.getResultList() returning the category list
        when(typedQuery3.getResultList()).thenReturn(categoryList);

        // Act
        List<Category> result = categoryDaoImpl.findAll();

        // Assert
        assertEquals(2, result.size());  // Verify that 2 categories are returned
        assertEquals("Electronics", result.get(0).getName());  // Verify the first category's name
        assertEquals("Books", result.get(1).getName());  // Verify the second category's name

        // Verify that the entityManager and typedQuery were called correctly
        verify(entityManager).createQuery(jpql, Category.class);
        verify(typedQuery3).getResultList();
    }
    
    
    @Test
    void testFindAll_Pageable() {
        // Arrange
        String jpql = "SELECT c FROM Category c";
        Pageable pageable = (Pageable) PageRequest.of(0, 2);  // First page, 2 items per page

        Category category1 = new Category();
        category1.setId((int) 1L);
        category1.setName("Electronics");

        Category category2 = new Category();
        category2.setId((int) 2L);
        category2.setName("Books");

        List<Category> categoryList = Arrays.asList(category1, category2);

        // Simulate entityManager.createQuery() for getting categories
        when(entityManager.createQuery(jpql, Category.class)).thenReturn(typedQuery4);
        when(typedQuery4.getResultList()).thenReturn(categoryList);
        when(typedQuery4.setFirstResult(0)).thenReturn(typedQuery4);  // Offset = 0 for the first page
        when(typedQuery4.setMaxResults(2)).thenReturn(typedQuery4);   // Page size = 2

        // Simulate entityManager.createQuery() for counting the total number of categories
        when(entityManager.createQuery("SELECT COUNT(c) FROM Category c", Long.class)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(10L);  // Simulating a total of 10 categories

        // Act
        Page<Category> result = categoryDaoImpl.findAll(pageable);
        
        // Assert
        assertEquals(2, result.getNumberOfElements());  // Verify 2 elements are returned on the first page
        assertEquals(10, result.getTotalElements());    // Verify total elements count is 10
        assertEquals("Electronics", result.getContent().get(0).getName());  // Verify the first category's name
        assertEquals("Books", result.getContent().get(1).getName());        // Verify the second category's name

        // Verify that the query and count query were called correctly
        verify(entityManager).createQuery(jpql, Category.class);
        verify(typedQuery4).setFirstResult(0);  // Verify pagination offset
        verify(typedQuery4).setMaxResults(2);   // Verify pagination limit
        verify(typedQuery4).getResultList();    // Verify result list retrieval
        verify(entityManager).createQuery("SELECT COUNT(c) FROM Category c", Long.class);
        verify(countQuery).getSingleResult();  // Verify total count retrieval
    }

}
