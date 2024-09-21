package com.ecom.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.dao.UserDao;
import com.ecom.model.UserDtls;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserDtls findByEmail(String email) {
        String jpql = "SELECT u FROM UserDtls u WHERE u.email = :email";
        TypedQuery<UserDtls> query = entityManager.createQuery(jpql, UserDtls.class);
        query.setParameter("email", email);
        List<UserDtls> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<UserDtls> findByRole(String role) {
        String jpql = "SELECT u FROM UserDtls u WHERE u.role = :role";
        TypedQuery<UserDtls> query = entityManager.createQuery(jpql, UserDtls.class);
        query.setParameter("role", role);
        return query.getResultList();
    }

    @Override
    public UserDtls findByResetToken(String token) {
        String jpql = "SELECT u FROM UserDtls u WHERE u.resetToken = :token";
        TypedQuery<UserDtls> query = entityManager.createQuery(jpql, UserDtls.class);
        query.setParameter("token", token);
        List<UserDtls> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public Boolean existsByEmail(String email) {
        String jpql = "SELECT COUNT(u) FROM UserDtls u WHERE u.email = :email";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("email", email);
        Long count = query.getSingleResult();
        return count > 0;
    }

    @Override
    public Optional<UserDtls> findById(int id) {
        UserDtls user = entityManager.find(UserDtls.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public UserDtls save(UserDtls user) {
        if (user.getId() == null) {  // Check if the user ID is null
            // New user, use persist
            entityManager.persist(user);
        } else {
            // Existing user, use merge
            user = entityManager.merge(user);
        }
        return user;
    }

    @Override
    public void delete(UserDtls user) {
        if (entityManager.contains(user)) {
            entityManager.remove(user);
        } else {
            entityManager.remove(entityManager.merge(user));  // Ensures managed entity is deleted
        }
    }
}
