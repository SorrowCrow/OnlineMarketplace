package com.OnlineMarketplace.OnlineMarketplace.Order;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.OnlineMarketplace.OnlineMarketplace.User.User;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create/{listingId}/{buyerId}")
    public Order createOrder(@PathVariable("listingId") Long listingId) {
        return orderService.createOrder( listingId);
    }
    @GetMapping("/get all orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders() {
        List<Order> orders = orderService.getOrdersByBuyer();
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/get order/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}



