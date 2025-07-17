package com.ecommerce.infrastructure.repository;

import com.ecommerce.application.repository.UserRepository;
import com.ecommerce.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositoryImpl extends JpaRepository<User, Long>, UserRepository {
    
    @Override
    default List<User> findAllUsers() {
        return findAll();
    }
    
    @Override
    default Optional<User> findUserById(Long id) {
        return findById(id);
    }
    
    @Override
    default Optional<User> findUserByEmail(String email) {
        return findByEmail(email);
    }
    
    @Override
    default User saveUser(User user) {
        return save(user);
    }
    
    @Override
    default void deleteById(Long id) {
        deleteById(id);
    }
    
    Optional<User> findByEmail(String email);
} 