package com.ecom.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ecom.model.Category;

public interface CategoryDao {

    Boolean existsByName(String name);

    List<Category> findByIsActiveTrue();

    Category save(Category category);

    void delete(Category category);

    Optional<Category> findById(Integer id);

    List<Category> findAll();

    Page<Category> findAll(Pageable pageable);  // For pagination support
}
