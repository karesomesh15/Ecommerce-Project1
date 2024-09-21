package com.ecom.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecom.dao.ProductDao;
import com.ecom.dao.UserDao;
import com.ecom.model.Cart;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.repository.CartRepository;
import com.ecom.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserDao userRepository;

	@Autowired
	private ProductDao productRepository;
	
	

    @Override
    public Cart saveCart(Integer productId, Integer userId, Integer quantity) {
        UserDtls userDtls = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Cart cart = cartRepository.findByProductIdAndUserId(productId, userId);

        if (cart == null) {
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(userDtls);
            cart.setQuantity(quantity); // Use the provided quantity
        } else {
            cart.setQuantity(cart.getQuantity() + quantity); // Update quantity
        }
        cart.setTotalPrice(cart.getQuantity() * product.getDiscountPrice());
        return cartRepository.save(cart);
    }
	
	
	@Override
	public void updateQuantity(Integer cartId, Integer newQuantity) {
	    Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
	    
	    if (newQuantity <= 0) {
	        cartRepository.delete(cart); // Remove cart item if quantity is 0 or less
	    } else {
	        cart.setQuantity(newQuantity);
	        cart.setTotalPrice(newQuantity * cart.getProduct().getDiscountPrice());
	        cartRepository.save(cart);
	    }
	}
    @Override
    public void removeFromCart(int cartId) {
        Optional<Cart> cart = cartRepository.findById(cartId);

        if (cart.isPresent()) {
            cartRepository.delete(cart.get());
        } else {
            throw new IllegalArgumentException("Cart item not found");
        }
    }


	@Override
	public Cart saveCart(Integer productId, Integer userId) {

		UserDtls userDtls = userRepository.findById(userId).get();
		Product product = productRepository.findById(productId).get();

		Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);

		Cart cart = null;

		if (ObjectUtils.isEmpty(cartStatus)) {
			cart = new Cart();
			cart.setProduct(product);
			cart.setUser(userDtls);
			cart.setQuantity(1);
			cart.setTotalPrice(1 * product.getDiscountPrice());
		} else {
			cart = cartStatus;
			cart.setQuantity(cart.getQuantity() + 1);
			cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());
		}
		Cart saveCart = cartRepository.save(cart);

		return saveCart;
	}

	@Override
	public List<Cart> getCartsByUser(Integer userId) {
		List<Cart> carts = cartRepository.findByUserId(userId);

		Double totalOrderPrice = 0.0;
		List<Cart> updateCarts = new ArrayList<>();
		for (Cart c : carts) {
			Double totalPrice = (c.getProduct().getDiscountPrice() * c.getQuantity());
			c.setTotalPrice(totalPrice);
			totalOrderPrice = totalOrderPrice + totalPrice;
			c.setTotalOrderPrice(totalOrderPrice);
			updateCarts.add(c);
		}

		return updateCarts;
	}

	@Override
	public Integer getCountCart(Integer userId) {
		Integer countByUserId = cartRepository.countByUserId(userId);
		return countByUserId;
	}

	@Override
	public void updateQuantity(String sy, Integer cid) {

		Cart cart = cartRepository.findById(cid).get();
		int updateQuantity;

		if (sy.equalsIgnoreCase("de")) {
			updateQuantity = cart.getQuantity() - 1;

			if (updateQuantity <= 0) {
				cartRepository.delete(cart);
			} else {
				cart.setQuantity(updateQuantity);
				cartRepository.save(cart);
			}

		} else {
			updateQuantity = cart.getQuantity() + 1;
			cart.setQuantity(updateQuantity);
			cartRepository.save(cart);
		}

	}

}
