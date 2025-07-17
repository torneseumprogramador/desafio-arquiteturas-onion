package com.ecommerce.application.repository;

import com.ecommerce.domain.OrderProduct;
import java.util.List;
import java.util.Optional;

/**
 * OrderProductRepository interface - defines operations for OrderProduct entity
 * This interface will be implemented in the infrastructure layer
 */
public interface OrderProductRepository {
    
    /**
     * Save an order product
     * @param orderProduct the order product to save
     * @return the saved order product
     */
    OrderProduct save(OrderProduct orderProduct);
    
    /**
     * Find order products by order id
     * @param orderId the order id
     * @return list of order products for the order
     */
    List<OrderProduct> findByOrderId(Long orderId);
    
    /**
     * Find order products by product id
     * @param productId the product id
     * @return list of order products for the product
     */
    List<OrderProduct> findByProductId(Long productId);
    
    /**
     * Delete order products by order id
     * @param orderId the order id
     */
    void deleteByOrderId(Long orderId);
    
    /**
     * Delete order products by product id
     * @param productId the product id
     */
    void deleteByProductId(Long productId);

    Optional<OrderProduct> findById(Long id);
    List<OrderProduct> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
} 