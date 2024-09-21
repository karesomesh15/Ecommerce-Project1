package com.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.model.Wishlist;
import com.ecom.model.UserDtls;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUser(UserDtls user);

    // Changed Long to Integer for productId
    Wishlist findByUserAndProductId(UserDtls user, Integer productId);
}