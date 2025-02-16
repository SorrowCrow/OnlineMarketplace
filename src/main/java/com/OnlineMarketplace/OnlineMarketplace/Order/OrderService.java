package com.OnlineMarketplace.OnlineMarketplace.Order;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserRepository;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import com.OnlineMarketplace.OnlineMarketplace.listing.ListingRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ListingRepository listingRepository;

    public Order createOrder(Long listingId) {
        // User buyer = userRepository.findById(buyerId)
        // .orElseThrow(() -> new RuntimeException("User not found"));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) {
            throw new AccessDeniedException("Unauthorized access");
        }
        String email = ((UserDetails) principal).getUsername();
        User buyer = userRepository.findByEmail(email)

                .orElseThrow(() -> new RuntimeException("User not found"));
        Listing listing = listingRepository.findById(listingId)

                .orElseThrow(() -> new RuntimeException("Listing not found"));

        Order order = new Order();
        order.setBuyer(buyer);
        order.setSeller(listing.getUser());

        order.setTitle(listing.getTitle());
        order.setDescription(listing.getDescription());
        order.setType(listing.getType());
        order.setPrice(listing.getPrice());
        order.setUnit(listing.getUnit());
        order.setStartDate(listing.getStartDate());
        order.setEndDate(listing.getEndDate());
        order.setCategory(listing.getCategory());
        order.setLocation(listing.getLocation());

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByBuyer() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) {
            throw new AccessDeniedException("Unauthorized access");
        }

        String email = ((UserDetails) principal).getUsername();
        User buyer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByBuyerId(buyer.getId());
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
