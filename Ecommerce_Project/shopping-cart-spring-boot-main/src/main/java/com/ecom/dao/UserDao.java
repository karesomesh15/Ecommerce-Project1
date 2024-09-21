package com.ecom.dao;

import java.util.List;
import java.util.Optional;

import com.ecom.model.UserDtls;

public interface UserDao {

    UserDtls findByEmail(String email);

    List<UserDtls> findByRole(String role);

    UserDtls findByResetToken(String token);

    Boolean existsByEmail(String email);

    Optional<UserDtls> findById(int id);  // Method to find a user by ID

    UserDtls save(UserDtls user);  // Method to save or update a user

    void delete(UserDtls user);  // Method to delete a user
}
