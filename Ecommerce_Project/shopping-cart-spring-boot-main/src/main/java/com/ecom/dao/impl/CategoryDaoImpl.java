package com.ecom.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.dao.CategoryDao;
import com.ecom.model.Category;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
@Transactional
public class CategoryDaoImpl implements CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean existsByName(String name) {
        String jpql = "SELECT COUNT(c) FROM Category c WHERE c.name = :name";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("name", name);
        Long count = query.getSingleResult();
        return count > 0;
    }

    @Override
    public List<Category> findByIsActiveTrue() {
        String jpql = "SELECT c FROM Category c WHERE c.isActive = true";
        TypedQuery<Category> query = entityManager.createQuery(jpql, Category.class);
        return query.getResultList();
    }

    @Override
    public Category save(Category category) {
        if (category.getId() == null) {
            entityManager.persist(category);
        } else {
            entityManager.merge(category);
        }
        return category;
    }

    @Override
    public void delete(Category category) {
        if (entityManager.contains(category)) {
            entityManager.remove(category);
        } else {
            entityManager.remove(entityManager.merge(category));
        }
    }

    @Override
    public Optional<Category> findById(Integer id) {
        Category category = entityManager.find(Category.class, id);
        return Optional.ofNullable(category);
    }

    @Override
    public List<Category> findAll() {
        String jpql = "SELECT c FROM Category c";
        TypedQuery<Category> query = entityManager.createQuery(jpql, Category.class);
        return query.getResultList();
    }

    // Pagination support
    @Override
    public Page<Category> findAll(Pageable pageable) {
        String jpql = "SELECT c FROM Category c";
        TypedQuery<Category> query = entityManager.createQuery(jpql, Category.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Category> results = query.getResultList();

        // Count the total number of records for pagination
        TypedQuery<Long> countQuery = entityManager.createQuery("SELECT COUNT(c) FROM Category c", Long.class);
        Long count = countQuery.getSingleResult();

        return new PageImpl<>(results, pageable, count);
    }
}
