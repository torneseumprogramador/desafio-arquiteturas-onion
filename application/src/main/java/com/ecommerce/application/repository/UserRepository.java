package com.ecommerce.application.repository;

import com.ecommerce.domain.User;
import java.util.List;
import java.util.Optional;

/**
 * UserRepository interface - defines operations for User entity
 * This interface will be implemented in the infrastructure layer
 */
public interface UserRepository {
    
    /**
     * Save a user
     * @param user the user to save
     * @return the saved user with generated id
     */
    User saveUser(User user);
    
    /**
     * Find a user by id
     * @param id the user id
     * @return Optional containing the user if found
     */
    Optional<User> findUserById(Long id);
    
    /**
     * Find all users
     * @return list of all users
     */
    List<User> findAllUsers();
    
    /**
     * Find a user by email
     * @param email the user email
     * @return Optional containing the user if found
     */
    Optional<User> findUserByEmail(String email);
    
    /**
     * Delete a user by id
     * @param id the user id
     */
    void deleteUser(Long id);
    
    /**
     * Check if a user exists by id
     * @param id the user id
     * @return true if user exists, false otherwise
     */
    boolean existsById(Long id);
} 