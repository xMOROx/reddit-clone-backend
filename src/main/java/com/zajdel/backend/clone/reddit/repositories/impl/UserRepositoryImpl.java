package com.zajdel.backend.clone.reddit.repositories.impl;

import com.zajdel.backend.clone.reddit.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class UserRepositoryImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public UserRepositoryImpl() {
    }
    @SuppressWarnings("unsued")
    @Transactional
    public Optional<User> findUserByUsername(String username) {
        String query = "SELECT u FROM Users u WHERE u.username like :username";
        TypedQuery<User> typedQuery = entityManager.createQuery(query, User.class);
        typedQuery.setParameter("username", username);
        try {
            return Optional.of(typedQuery.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    @SuppressWarnings("unsued")
    @Transactional
    public Optional<User> findUserByEmail(String email) {
        String query = "SELECT u FROM Users u WHERE u.email like :email";
        TypedQuery<User> typedQuery = entityManager.createQuery(query, User.class);
        typedQuery.setParameter("email", email);
        try {
            return Optional.of(typedQuery.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
