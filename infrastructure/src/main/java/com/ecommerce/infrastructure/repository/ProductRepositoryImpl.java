package com.ecommerce.infrastructure.repository;

import com.ecommerce.application.repository.ProductRepository;
import com.ecommerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepositoryImpl extends JpaRepository<Product, Long>, ProductRepository {
    
    @Override
    default List<Product> findAllProducts() {
        return findAll();
    }
    
    @Override
    default Optional<Product> findProductById(Long id) {
        return findById(id);
    }
    
    @Override
    default Product saveProduct(Product product) {
        return save(product);
    }
    
    @Override
    default void deleteProduct(Long id) {
        deleteById(id);
    }
    
    List<Product> findByNameContainingIgnoreCase(String name);
} 