package com.ecommerce.infrastructure.repository;

import com.ecommerce.application.repository.OrderRepository;
import com.ecommerce.domain.Order;
import com.ecommerce.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepositoryImpl extends JpaRepository<Order, Long>, OrderRepository {
    
    @Override
    default List<Order> findAllOrders() {
        return findAll();
    }
    
    @Override
    default Optional<Order> findOrderById(Long id) {
        return findById(id);
    }
    
    @Override
    default Order saveOrder(Order order) {
        return save(order);
    }
    
    @Override
    default void deleteById(Long id) {
        deleteById(id);
    }
    
    List<Order> findByUser(User user);
    
    List<Order> findByUserOrderByOrderDateDesc(User user);
    
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersByDateRange(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
    List<Order> findByStatus(Order.OrderStatus status);
} 