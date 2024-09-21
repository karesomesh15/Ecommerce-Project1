package com.ecom.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.dao.ProductOrderDao;
import com.ecom.model.ProductOrder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
@Transactional
public class ProductOrderDaoImpl implements ProductOrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProductOrder> findByUserId(Integer userId) {
        String jpql = "SELECT po FROM ProductOrder po WHERE po.user.id = :userId";
        TypedQuery<ProductOrder> query = entityManager.createQuery(jpql, ProductOrder.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public Optional<ProductOrder> findByOrderId(String orderId) {
        String jpql = "SELECT po FROM ProductOrder po WHERE po.orderId = :orderId";
        TypedQuery<ProductOrder> query = entityManager.createQuery(jpql, ProductOrder.class);
        query.setParameter("orderId", orderId);
        List<ProductOrder> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public ProductOrder save(ProductOrder productOrder) {
        if (productOrder.getId() == null) {
            entityManager.persist(productOrder);
        } else {
            productOrder = entityManager.merge(productOrder);
        }
        return productOrder;
    }

    @Override
    public void delete(ProductOrder productOrder) {
        if (entityManager.contains(productOrder)) {
            entityManager.remove(productOrder);
        } else {
            entityManager.remove(entityManager.merge(productOrder));
        }
    }

    @Override
    public Optional<ProductOrder> findById(Integer id) {
        ProductOrder productOrder = entityManager.find(ProductOrder.class, id);
        return Optional.ofNullable(productOrder);
    }

    @Override
    public List<ProductOrder> findAll() {
        String jpql = "SELECT po FROM ProductOrder po";
        TypedQuery<ProductOrder> query = entityManager.createQuery(jpql, ProductOrder.class);
        return query.getResultList();
    }

    @Override
    public Page<ProductOrder> findAll(Pageable pageable) {
        String jpql = "SELECT po FROM ProductOrder po";
        TypedQuery<ProductOrder> query = entityManager.createQuery(jpql, ProductOrder.class);
        
        // Apply pagination
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<ProductOrder> orders = query.getResultList();

        // Get the total count of ProductOrder records
        String countJpql = "SELECT COUNT(po) FROM ProductOrder po";
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
        Long totalRecords = countQuery.getSingleResult();

        return new PageImpl<>(orders, pageable, totalRecords);
    }
}
