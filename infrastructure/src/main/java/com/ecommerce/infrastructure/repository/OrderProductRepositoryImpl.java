package com.ecommerce.infrastructure.repository;

import com.ecommerce.application.repository.OrderProductRepository;
import com.ecommerce.domain.Order;
import com.ecommerce.domain.OrderProduct;
import com.ecommerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderProductRepositoryImpl extends JpaRepository<OrderProduct, Long>, OrderProductRepository {
    
    @Override
    default List<OrderProduct> findAllOrderProducts() {
        return findAll();
    }
    
    @Override
    default Optional<OrderProduct> findOrderProductById(Long id) {
        return findById(id);
    }
    
    @Override
    default OrderProduct saveOrderProduct(OrderProduct orderProduct) {
        return save(orderProduct);
    }
    
    @Override
    default void deleteOrderProduct(Long id) {
        deleteById(id);
    }
    
    List<OrderProduct> findByOrder(Order order);
    
    List<OrderProduct> findByProduct(Product product);
    
    List<OrderProduct> findByOrderAndProduct(Order order, Product product);
} 