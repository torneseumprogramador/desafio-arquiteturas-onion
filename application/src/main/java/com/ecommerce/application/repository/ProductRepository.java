package com.ecommerce.application.repository;

import com.ecommerce.domain.Product;
import java.util.List;
import java.util.Optional;

/**
 * ProductRepository interface - defines operations for Product entity
 * This interface will be implemented in the infrastructure layer
 */
public interface ProductRepository {
    
    /**
     * Save a product
     * @param product the product to save
     * @return the saved product with generated id
     */
    Product saveProduct(Product product);
    
    /**
     * Find a product by id
     * @param id the product id
     * @return Optional containing the product if found
     */
    Optional<Product> findProductById(Long id);
    
    /**
     * Find all products
     * @return list of all products
     */
    List<Product> findAllProducts();
    
    /**
     * Find products by name containing the given string
     * @param name the name to search for
     * @return list of products matching the name
     */
    List<Product> findByNameContaining(String name);
    
    /**
     * Delete a product by id
     * @param id the product id
     */
    void deleteById(Long id);
    
    /**
     * Check if a product exists by id
     * @param id the product id
     * @return true if product exists, false otherwise
     */
    boolean existsById(Long id);
} 