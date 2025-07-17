package com.ecommerce.application.repository;

import com.ecommerce.domain.Order;
import com.ecommerce.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * OrderRepository interface - defines operations for Order entity
 * This interface will be implemented in the infrastructure layer
 */
public interface OrderRepository {
    
    /**
     * Save an order
     * @param order the order to save
     * @return the saved order with generated id
     */
    Order saveOrder(Order order);
    
    /**
     * Find an order by id
     * @param id the order id
     * @return Optional containing the order if found
     */
    Optional<Order> findOrderById(Long id);
    
    /**
     * Find all orders
     * @return list of all orders
     */
    List<Order> findAllOrders();
    
    /**
     * Find orders by user
     * @param user the user
     * @return list of orders for the user
     */
    List<Order> findByUser(User user);
    
    /**
     * Find orders by date range
     * @param startDate the start date
     * @param endDate the end date
     * @return list of orders in the date range
     */
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Delete an order by id
     * @param id the order id
     */
    void deleteOrder(Long id);
    
    /**
     * Check if an order exists by id
     * @param id the order id
     * @return true if order exists, false otherwise
     */
    boolean existsById(Long id);
} 