package com.ecommerce.application.service;

import com.ecommerce.application.repository.OrderRepository;
import com.ecommerce.application.repository.OrderProductRepository;
import com.ecommerce.application.repository.UserRepository;
import com.ecommerce.application.repository.ProductRepository;
import com.ecommerce.domain.Order;
import com.ecommerce.domain.OrderProduct;
import com.ecommerce.domain.User;
import com.ecommerce.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * OrderService - implements business logic for Order entity
 */
@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    
    @Autowired
    public OrderService(OrderRepository orderRepository, 
                       OrderProductRepository orderProductRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }
    
    /**
     * Create a new order
     * @param order the order to create
     * @return the created order with generated id
     */
    public Order createOrder(Order order) {
        // Validate order data
        if (order.getUser() == null) {
            throw new IllegalArgumentException("Order user cannot be null");
        }
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }
        
        // Check if user exists
        if (!userRepository.existsById(order.getUser().getId())) {
            throw new IllegalArgumentException("User with id " + order.getUser().getId() + " does not exist");
        }
        
        // Save the order first
        Order savedOrder = orderRepository.saveOrder(order);
        
        // Save order products if any
        if (order.getOrderProducts() != null && !order.getOrderProducts().isEmpty()) {
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                orderProduct.setOrder(savedOrder);
                validateOrderProduct(orderProduct);
                orderProductRepository.save(orderProduct);
            }
        }
        
        return savedOrder;
    }
    
    /**
     * Get order by id
     * @param id the order id
     * @return Optional containing the order if found
     */
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findOrderById(id);
    }
    
    /**
     * Get all orders
     * @return list of all orders
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAllOrders();
    }
    
    /**
     * Get orders by user
     * @param user the user
     * @return list of orders for the user
     */
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
    
    /**
     * Get orders by date range
     * @param startDate the start date
     * @param endDate the end date
     * @return list of orders in the date range
     */
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
    
    /**
     * Delete order by id
     * @param id the order id
     */
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException("Order with id " + id + " does not exist");
        }
        
        // Delete the order (order products will be deleted by cascade)
        orderRepository.deleteById(id);
    }
    
    /**
     * Add product to order
     * @param orderId the order id
     * @param productId the product id
     * @param quantity the quantity
     * @return the updated order
     */
    public Order addProductToOrder(Long orderId, Long productId, Integer quantity) {
        // Validate order exists
        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order with id " + orderId + " does not exist"));
        
        // Validate product exists
        Product product = productRepository.findProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + productId + " does not exist"));
        
        // Create order product
        OrderProduct orderProduct = new OrderProduct(order, product, quantity, product.getPrice());
        validateOrderProduct(orderProduct);
        
        // Add to order
        order.addOrderProduct(orderProduct);
        
        return orderRepository.saveOrder(order);
    }
    
    /**
     * Update order status
     * @param orderId the order id
     * @param status the new status
     * @return the updated order
     */
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order with id " + orderId + " does not exist"));
        
        order.setStatus(status);
        return orderRepository.saveOrder(order);
    }
    
    /**
     * Remove product from order
     * @param orderId the order id
     * @param productId the product id
     * @return the updated order
     */
    public Order removeProductFromOrder(Long orderId, Long productId) {
        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order with id " + orderId + " does not exist"));
        
        Product product = productRepository.findProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + productId + " does not exist"));
        
        // Find and remove the order product
        order.getOrderProducts().removeIf(op -> op.getProduct().getId().equals(productId));
        
        return orderRepository.saveOrder(order);
    }
    
    /**
     * Add product to order (overloaded method for existing order)
     * @param order the existing order
     * @param productId the product id
     * @param quantity the quantity
     * @return the updated order
     */
    public Order addProductToOrder(Order order, Long productId, Integer quantity) {
        // Validate product exists
        Product product = productRepository.findProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + productId + " does not exist"));
        
        // Create order product
        OrderProduct orderProduct = new OrderProduct(order, product, quantity, product.getPrice());
        validateOrderProduct(orderProduct);
        
        // Add to order
        order.addOrderProduct(orderProduct);
        
        return orderRepository.saveOrder(order);
    }
    
    /**
     * Validate order product
     * @param orderProduct the order product to validate
     */
    private void validateOrderProduct(OrderProduct orderProduct) {
        if (orderProduct.getQuantity() == null || orderProduct.getQuantity() <= 0) {
            throw new IllegalArgumentException("Order product quantity must be greater than zero");
        }
    }
} 