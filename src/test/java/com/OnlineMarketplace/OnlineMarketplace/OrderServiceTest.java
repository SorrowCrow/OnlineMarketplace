package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.Order.Order;
import com.OnlineMarketplace.OnlineMarketplace.Order.OrderRepository;
import com.OnlineMarketplace.OnlineMarketplace.Order.OrderService;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserRepository;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import com.OnlineMarketplace.OnlineMarketplace.listing.ListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private OrderService orderService;

    private User buyer;
    private User seller;
    private Listing listing;
    private Order order;

    @BeforeEach
    void setUp() {
        buyer = new User();
        buyer.setId(1L);
        buyer.setEmail("buyer@example.com");

        seller = new User();
        seller.setId(2L);
        seller.setEmail("seller@example.com");

        listing = new Listing();
        listing.setListingID(100L);
        listing.setUser(seller);
        listing.setTitle("Test Listing");
        listing.setDescription("Test Description");
        listing.setPrice(new BigDecimal("100.00"));

        order = new Order();
        order.setId(200L);
        order.setBuyer(buyer);
        order.setSeller(seller);
        order.setTitle(listing.getTitle());
    }

    @Test
    void testCreateOrder_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(buyer));
        when(listingRepository.findById(100L)).thenReturn(Optional.of(listing));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.createOrder(1L, 100L);

        assertNotNull(createdOrder);
        assertEquals("Test Listing", createdOrder.getTitle());
        assertEquals(buyer, createdOrder.getBuyer());
        assertEquals(seller, createdOrder.getSeller());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrder_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.createOrder(1L, 100L));
        assertEquals("User not found", exception.getMessage());

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testCreateOrder_ListingNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(buyer));
        when(listingRepository.findById(100L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.createOrder(1L, 100L));
        assertEquals("Listing not found", exception.getMessage());

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<Order> orders = orderService.getAllOrders();

        assertEquals(1, orders.size());
        assertEquals(200L, orders.get(0).getId());
    }

    @Test
    void testGetOrderById_Found() {
        when(orderRepository.findById(200L)).thenReturn(Optional.of(order));

        Optional<Order> foundOrder = orderService.getOrderById(200L);

        assertTrue(foundOrder.isPresent());
        assertEquals(200L, foundOrder.get().getId());
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(200L)).thenReturn(Optional.empty());

        Optional<Order> foundOrder = orderService.getOrderById(200L);

        assertFalse(foundOrder.isPresent());
    }

    @Test
    void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(200L);

        orderService.deleteOrder(200L);

        verify(orderRepository, times(1)).deleteById(200L);
    }
}
