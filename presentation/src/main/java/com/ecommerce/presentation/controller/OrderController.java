package com.ecommerce.presentation.controller;

import com.ecommerce.application.service.OrderService;
import com.ecommerce.application.service.UserService;
import com.ecommerce.domain.Order;
import com.ecommerce.domain.User;
import com.ecommerce.presentation.dto.OrderDto;
import com.ecommerce.presentation.dto.OrderProductDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderDto> orderDtos = orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        try {
            User user = userService.getUserById(orderDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
            
            Order order = new Order(user);
            
            // Adicionar produtos ao pedido
            if (orderDto.getOrderProducts() != null) {
                for (OrderProductDto orderProductDto : orderDto.getOrderProducts()) {
                    orderService.addProductToOrder(order, orderProductDto.getProductId(), 
                            orderProductDto.getQuantity());
                }
            }
            
            Order savedOrder = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertToDto(savedOrder));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Long id, 
                                                    @RequestParam Order.OrderStatus status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(convertToDto(updatedOrder));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
            
            List<Order> orders = orderService.getOrdersByUser(user);
            List<OrderDto> orderDtos = orders.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(orderDtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{orderId}/products")
    public ResponseEntity<OrderDto> addProductToOrder(@PathVariable Long orderId,
                                                    @RequestParam Long productId,
                                                    @RequestParam Integer quantity) {
        try {
            Order updatedOrder = orderService.addProductToOrder(orderId, productId, quantity);
            return ResponseEntity.ok(convertToDto(updatedOrder));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{orderId}/products/{productId}")
    public ResponseEntity<OrderDto> removeProductFromOrder(@PathVariable Long orderId,
                                                         @PathVariable Long productId) {
        try {
            Order updatedOrder = orderService.removeProductFromOrder(orderId, productId);
            return ResponseEntity.ok(convertToDto(updatedOrder));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    private OrderDto convertToDto(Order order) {
        List<OrderProductDto> orderProductDtos = order.getOrderProducts().stream()
                .map(op -> new OrderProductDto(
                        op.getId(),
                        op.getProduct().getId(),
                        op.getQuantity(),
                        op.getPrice(),
                        op.getProduct().getName()
                ))
                .collect(Collectors.toList());
        
        return new OrderDto(
                order.getId(),
                order.getUser().getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus(),
                orderProductDtos
        );
    }
} 