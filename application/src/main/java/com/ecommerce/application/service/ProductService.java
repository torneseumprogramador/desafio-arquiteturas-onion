package com.ecommerce.application.service;

import com.ecommerce.application.repository.ProductRepository;
import com.ecommerce.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ProductService - implements business logic for Product entity
 */
@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    /**
     * Create a new product
     * @param product the product to create
     * @return the created product with generated id
     */
    public Product createProduct(Product product) {
        // Validate product data
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
        
        return productRepository.saveProduct(product);
    }
    
    /**
     * Get product by id
     * @param id the product id
     * @return Optional containing the product if found
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findProductById(id);
    }
    
    /**
     * Get all products
     * @return list of all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAllProducts();
    }
    
    /**
     * Search products by name
     * @param name the name to search for
     * @return list of products matching the name
     */
    public List<Product> searchProductsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findByNameContaining(name.trim());
    }
    
    /**
     * Update product
     * @param product the product to update
     * @return the updated product
     */
    public Product updateProduct(Product product) {
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product id cannot be null for update");
        }
        if (!productRepository.existsById(product.getId())) {
            throw new IllegalArgumentException("Product with id " + product.getId() + " does not exist");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
        
        return productRepository.saveProduct(product);
    }
    
    /**
     * Delete product by id
     * @param id the product id
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product with id " + id + " does not exist");
        }
        productRepository.deleteById(id);
    }
} 