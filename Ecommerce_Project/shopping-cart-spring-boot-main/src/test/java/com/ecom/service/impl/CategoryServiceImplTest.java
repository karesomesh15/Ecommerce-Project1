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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.ecom.dao.CategoryDao;
import com.ecom.model.Category;

class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryDao categoryRepository;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        category1 = new Category();
        category1.setId(1);
        category1.setName("Electronics");
        category1.setIsActive(true);
        
        category2 = new Category();
        category2.setId(2);
        category2.setName("Books");
        category2.setIsActive(true);
    }

    @Test
    void testSaveCategory() {
        when(categoryRepository.save(category1)).thenReturn(category1);

        Category savedCategory = categoryService.saveCategory(category1);

        assertNotNull(savedCategory);
        assertEquals(category1.getName(), savedCategory.getName());
        verify(categoryRepository).save(category1);
    }

    @Test
    void testGetAllCategory() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<Category> categories = categoryService.getAllCategory();

        assertEquals(2, categories.size());
        assertEquals(category1.getName(), categories.get(0).getName());
        assertEquals(category2.getName(), categories.get(1).getName());
        verify(categoryRepository).findAll();
    }

    @Test
    void testExistCategory() {
        String name = "Electronics";
        when(categoryRepository.existsByName(name)).thenReturn(true);

        Boolean exists = categoryService.existCategory(name);

        assertTrue(exists);
        verify(categoryRepository).existsByName(name);
    }

    @Test
    void testDeleteCategory_Success() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category1));

        Boolean deleted = categoryService.deleteCategory(1);

        assertTrue(deleted);
        verify(categoryRepository).delete(category1);
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        Boolean deleted = categoryService.deleteCategory(1);

        assertFalse(deleted);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void testGetCategoryById_Found() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category1));

        Category foundCategory = categoryService.getCategoryById(1);

        assertNotNull(foundCategory);
        assertEquals(category1.getName(), foundCategory.getName());
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        Category foundCategory = categoryService.getCategoryById(1);

        assertNull(foundCategory);
    }

    @Test
    void testGetAllActiveCategory() {
        when(categoryRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(category1, category2));

        List<Category> activeCategories = categoryService.getAllActiveCategory();

        assertEquals(2, activeCategories.size());
        assertTrue(activeCategories.stream().allMatch(Category::getIsActive));
    }

    @Test
    void testGetAllCategorPagination() {
        Page<Category> page = new PageImpl<>(Arrays.asList(category1, category2));
        when(categoryRepository.findAll(PageRequest.of(0, 2))).thenReturn(page);

        Page<Category> resultPage = categoryService.getAllCategorPagination(0, 2);

        assertEquals(2, resultPage.getContent().size());
        assertEquals(category1.getName(), resultPage.getContent().get(0).getName());
    }
}
