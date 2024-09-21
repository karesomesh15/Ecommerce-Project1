package com.ecom.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecom.model.Product;

public interface ProductDao {

    // 1. Save a product (for create and update)
    Product save(Product product);

    // 2. Find a product by ID
    Optional<Product> findById(Integer id);

    // 3. Delete a product
    void delete(Product product);

    // 4. Find all products
    List<Product> findAll();

    // 5. Find all products with pagination
    Page<Product> findAll(Pageable pageable);

    // 6. Find active products
    List<Product> findByIsActiveTrue();

    // 7. Find active products with pagination
    Page<Product> findByIsActiveTrue(Pageable pageable);

    // 8. Find products by category
    List<Product> findByCategory(String category);

    // 9. Find products by category with pagination
    Page<Product> findByCategory(Pageable pageable, String category);

    // 10. Search products by title or category (case-insensitive)
    List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2);

    // 11. Search products by title or category with pagination
    Page<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2, Pageable pageable);

    // 12. Find active products by title or category with pagination
    Page<Product> findByIsActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2, Pageable pageable);
}
