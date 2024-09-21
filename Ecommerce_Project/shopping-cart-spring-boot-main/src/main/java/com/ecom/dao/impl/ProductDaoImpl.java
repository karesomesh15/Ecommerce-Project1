package com.ecom.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.dao.ProductDao;
import com.ecom.model.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
@Transactional
public class ProductDaoImpl implements ProductDao {

    @PersistenceContext
    private EntityManager entityManager;

    // 1. Find All Products (Non-paginated)
    @Override
    public List<Product> findAll() {
        String jpql = "SELECT p FROM Product p";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
        return query.getResultList();
    }

    // 2. Find All Products with Pagination
    @Override
    public Page<Product> findAll(Pageable pageable) {
        String jpql = "SELECT p FROM Product p";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Product> results = query.getResultList();

        TypedQuery<Long> countQuery = entityManager.createQuery("SELECT COUNT(p) FROM Product p", Long.class);
        Long count = countQuery.getSingleResult();
        return new PageImpl<>(results, pageable, count);
    }

    // 3. Find Active Products and Search by Title/Category (Pagination)
    @Override
    public Page<Product> findByIsActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(
            String ch, String ch2, Pageable pageable) {
        String jpql = "SELECT p FROM Product p WHERE p.isActive = true AND " +
                      "(LOWER(p.title) LIKE LOWER(CONCAT('%', :ch, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%', :ch2, '%')))";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
        query.setParameter("ch", ch);
        query.setParameter("ch2", ch2);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Product> results = query.getResultList();

        TypedQuery<Long> countQuery = entityManager.createQuery(
                "SELECT COUNT(p) FROM Product p WHERE p.isActive = true AND " +
                "(LOWER(p.title) LIKE LOWER(CONCAT('%', :ch, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%', :ch2, '%')))", 
                Long.class);
        countQuery.setParameter("ch", ch);
        countQuery.setParameter("ch2", ch2);
        Long count = countQuery.getSingleResult();
        return new PageImpl<>(results, pageable, count);
    }

    // Other existing methods...

    @Override
    public List<Product> findByIsActiveTrue() {
        String jpql = "SELECT p FROM Product p WHERE p.isActive = true";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
        return query.getResultList();
    }

    @Override
    public Page<Product> findByIsActiveTrue(Pageable pageable) {
        String jpql = "SELECT p FROM Product p WHERE p.isActive = true";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Product> results = query.getResultList();

        TypedQuery<Long> countQuery = entityManager.createQuery("SELECT COUNT(p) FROM Product p WHERE p.isActive = true", Long.class);
        Long count = countQuery.getSingleResult();
        return new PageImpl<>(results, pageable, count);
    }

    @Override
    public List<Product> findByCategory(String category) {
        String jpql = "SELECT p FROM Product p WHERE p.category = :category";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
        query.setParameter("category", category);
        return query.getResultList();
    }

    @Override
    public List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2) {
        String jpql = "SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :ch, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%', :ch2, '%'))";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
        query.setParameter("ch", ch);
        query.setParameter("ch2", ch2);
        return query.getResultList();
    }

    @Override
    public Page<Product> findByCategory(Pageable pageable, String category) {
        String jpql = "SELECT p FROM Product p WHERE p.category = :category";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
        query.setParameter("category", category);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Product> results = query.getResultList();

        TypedQuery<Long> countQuery = entityManager.createQuery("SELECT COUNT(p) FROM Product p WHERE p.category = :category", Long.class);
        countQuery.setParameter("category", category);
        Long count = countQuery.getSingleResult();
        return new PageImpl<>(results, pageable, count);
    }

    @Override
    public Page<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2, Pageable pageable) {
        String jpql = "SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :ch, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%', :ch2, '%'))";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
        query.setParameter("ch", ch);
        query.setParameter("ch2", ch2);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Product> results = query.getResultList();

        TypedQuery<Long> countQuery = entityManager.createQuery("SELECT COUNT(p) FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :ch, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%', :ch2, '%'))", Long.class);
        countQuery.setParameter("ch", ch);
        countQuery.setParameter("ch2", ch2);
        Long count = countQuery.getSingleResult();
        return new PageImpl<>(results, pageable, count);
    }

    @Override
    public Optional<Product> findById(Integer id) {
        Product product = entityManager.find(Product.class, id);
        return Optional.ofNullable(product);
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            entityManager.persist(product);
        } else {
            product = entityManager.merge(product);
        }
        return product;
    }

    @Override
    public void delete(Product product) {
        if (entityManager.contains(product)) {
            entityManager.remove(product);
        } else {
            entityManager.remove(entityManager.merge(product));
        }
    }
}
