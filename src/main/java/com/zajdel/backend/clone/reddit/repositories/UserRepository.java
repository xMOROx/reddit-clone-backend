package com.zajdel.backend.clone.reddit.repositories;

import com.zajdel.backend.clone.reddit.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find user by username
     * @param username - username of the user
     * @return - user with the given username
     */
    @Transactional
    Optional<User> findUserByUsername(String username);
    /**
     * Find user by email
     * @param email - email of the user
     * @return - user with the given email
     */
    @Transactional
    Optional<User> findUserByEmail(String email);
}
