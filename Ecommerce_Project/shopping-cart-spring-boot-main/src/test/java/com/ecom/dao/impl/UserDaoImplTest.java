package com.ecom.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ecom.model.UserDtls;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class UserDaoImplTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserDaoImpl userDaoImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByEmail_Found() {
        // Arrange
        String email = "test@example.com";
        UserDtls expectedUser = new UserDtls();
        expectedUser.setEmail(email);

        TypedQuery<UserDtls> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(UserDtls.class))).thenReturn(query);
        when(query.setParameter("email", email)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(expectedUser));

        // Act
        UserDtls actualUser = userDaoImpl.findByEmail(email);

        // Assert
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void testFindByEmail_NotFound() {
        // Arrange
        String email = "test@example.com";

        TypedQuery<UserDtls> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(UserDtls.class))).thenReturn(query);
        when(query.setParameter("email", email)).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());

        // Act
        UserDtls actualUser = userDaoImpl.findByEmail(email);

        // Assert
        assertNull(actualUser);
    }

    @Test
    void testFindByRole() {
        // Arrange
        String role = "USER";
        List<UserDtls> expectedUsers = new ArrayList<>();
        expectedUsers.add(new UserDtls());

        TypedQuery<UserDtls> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(UserDtls.class))).thenReturn(query);
        when(query.setParameter("role", role)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedUsers);

        // Act
        List<UserDtls> actualUsers = userDaoImpl.findByRole(role);

        // Assert
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void testFindByResetToken_Found() {
        // Arrange
        String token = "reset-token";
        UserDtls expectedUser = new UserDtls();
        expectedUser.setResetToken(token);

        TypedQuery<UserDtls> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(UserDtls.class))).thenReturn(query);
        when(query.setParameter("token", token)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(expectedUser));

        // Act
        UserDtls actualUser = userDaoImpl.findByResetToken(token);

        // Assert
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void testFindByResetToken_NotFound() {
        // Arrange
        String token = "reset-token";

        TypedQuery<UserDtls> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(UserDtls.class))).thenReturn(query);
        when(query.setParameter("token", token)).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());

        // Act
        UserDtls actualUser = userDaoImpl.findByResetToken(token);

        // Assert
        assertNull(actualUser);
    }

    @Test
    void testExistsByEmail_True() {
        // Arrange
        String email = "test@example.com";

        TypedQuery<Long> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(query);
        when(query.setParameter("email", email)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1L);

        // Act
        Boolean exists = userDaoImpl.existsByEmail(email);

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistsByEmail_False() {
        // Arrange
        String email = "test@example.com";

        TypedQuery<Long> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(query);
        when(query.setParameter("email", email)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(0L);

        // Act
        Boolean exists = userDaoImpl.existsByEmail(email);

        // Assert
        assertFalse(exists);
    }

    @Test
    void testFindById_Found() {
        // Arrange
        int id = 1;
        UserDtls expectedUser = new UserDtls();
        expectedUser.setId(id);

        when(entityManager.find(UserDtls.class, id)).thenReturn(expectedUser);

        // Act
        Optional<UserDtls> actualUser = userDaoImpl.findById(id);

        // Assert
        assertTrue(actualUser.isPresent());
        assertEquals(expectedUser, actualUser.get());
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        int id = 1;

        when(entityManager.find(UserDtls.class, id)).thenReturn(null);

        // Act
        Optional<UserDtls> actualUser = userDaoImpl.findById(id);

        // Assert
        assertFalse(actualUser.isPresent());
    }

    @Test
    void testSave_NewUser() {
        // Arrange
        UserDtls newUser = new UserDtls();
        newUser.setEmail("new@example.com");

        // Act
        UserDtls savedUser = userDaoImpl.save(newUser);

        // Assert
        assertEquals(newUser, savedUser);
        verify(entityManager).persist(newUser);
    }

    @Test
    void testSave_ExistingUser() {
        // Arrange
        UserDtls existingUser = new UserDtls();
        existingUser.setId(1);
        existingUser.setEmail("existing@example.com");

        when(entityManager.merge(existingUser)).thenReturn(existingUser);

        // Act
        UserDtls savedUser = userDaoImpl.save(existingUser);

        // Assert
        assertEquals(existingUser, savedUser);
        verify(entityManager).merge(existingUser);
    }

    @Test
    void testDelete_ContainedUser() {
        // Arrange
        UserDtls user = new UserDtls();
        user.setId(1);

        when(entityManager.contains(user)).thenReturn(true);

        // Act
        userDaoImpl.delete(user);

        // Assert
        verify(entityManager).remove(user);
    }

    @Test
    void testDelete_NonContainedUser() {
        // Arrange
        UserDtls user = new UserDtls();
        user.setId(1);

        when(entityManager.contains(user)).thenReturn(false);
        when(entityManager.merge(user)).thenReturn(user);

        // Act
        userDaoImpl.delete(user);

        // Assert
        verify(entityManager).remove(user);
        verify(entityManager).merge(user);
    }
}
