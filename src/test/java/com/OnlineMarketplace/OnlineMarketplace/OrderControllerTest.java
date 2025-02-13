package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.Order.Order;
import com.OnlineMarketplace.OnlineMarketplace.Order.OrderController;
import com.OnlineMarketplace.OnlineMarketplace.Order.OrderService;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private Order order;
    private User buyer;
    private User seller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        buyer = new User();
        buyer.setId(1L);
        buyer.setEmail("buyer@example.com");

        seller = new User();
        seller.setId(2L);
        seller.setEmail("seller@example.com");

        order = new Order();
        order.setId(200L);
        order.setBuyer(buyer);
        order.setSeller(seller);
        order.setTitle("Test Listing");
        order.setPrice(new BigDecimal("100.00"));
    }

    @Test
    void testCreateOrder_Success() throws Exception {
        when(orderService.createOrder(1L, 100L)).thenReturn(order);

        mockMvc.perform(post("/api/orders/create/100/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(200L))
                .andExpect(jsonPath("$.title").value("Test Listing"));

        verify(orderService, times(1)).createOrder(1L, 100L);
    }

    @Test
    void testGetAllOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(order));

        mockMvc.perform(get("/api/orders/get all orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(200L))
                .andExpect(jsonPath("$[0].title").value("Test Listing"));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testGetOrderById_Found() throws Exception {
        when(orderService.getOrderById(200L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/api/orders/get order/200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(200L))
                .andExpect(jsonPath("$.title").value("Test Listing"));

        verify(orderService, times(1)).getOrderById(200L);
    }

    @Test
    void testGetOrderById_NotFound() throws Exception {
        when(orderService.getOrderById(200L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/get order/200"))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).getOrderById(200L);
    }

    @Test
    void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(200L);

        mockMvc.perform(delete("/api/orders/200"))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(200L);
    }
}
