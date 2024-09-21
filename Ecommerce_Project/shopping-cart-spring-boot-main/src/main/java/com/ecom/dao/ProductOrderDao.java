package com.ecom.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecom.model.ProductOrder;

public interface ProductOrderDao {

    List<ProductOrder> findByUserId(Integer userId);

    Optional<ProductOrder> findByOrderId(String orderId);

    ProductOrder save(ProductOrder productOrder);

    void delete(ProductOrder productOrder);

    Optional<ProductOrder> findById(Integer id);

    List<ProductOrder> findAll();

    Page<ProductOrder> findAll(Pageable pageable);  // Pagination support
}
