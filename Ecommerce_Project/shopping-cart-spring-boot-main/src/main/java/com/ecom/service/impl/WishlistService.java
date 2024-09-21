package com.ecom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.model.Wishlist;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.repository.WishlistRepository;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    public List<Wishlist> getWishlistByUser(UserDtls user) {
        return wishlistRepository.findByUser(user);
    }
    
    

    public Wishlist addToWishlist(UserDtls user, Integer productId) {
        Wishlist existingWishlist = wishlistRepository.findByUserAndProductId(user, productId);
        if (existingWishlist == null) {
            Wishlist wishlist = new Wishlist(user, new Product(productId)); // Use Product constructor with id
            return wishlistRepository.save(wishlist);
        }
        return null; // Already exists
    }

    public void removeFromWishlist(UserDtls user, Integer productId) {
        Wishlist wishlist = wishlistRepository.findByUserAndProductId(user, productId);
        if (wishlist != null) {
            wishlistRepository.delete(wishlist);
        }
    }
}