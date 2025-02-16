package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.Cart.Cart;
import com.OnlineMarketplace.OnlineMarketplace.Cart.CartRepository;
import com.OnlineMarketplace.OnlineMarketplace.Order.Order;
import com.OnlineMarketplace.OnlineMarketplace.Order.OrderRepository;
import com.OnlineMarketplace.OnlineMarketplace.Order.OrderService;
import com.OnlineMarketplace.OnlineMarketplace.Payment.PaymentService;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserRepository;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import com.OnlineMarketplace.OnlineMarketplace.listing.ListingRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.checkout.SessionCreateParams;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class PaymentServiceTests {
    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private PaymentService paymentService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        Stripe.apiKey = "${stripe.api.key}";
    }

    @Test
    public void testCreatePaymentSession() throws StripeException {
        try (MockedStatic<Customer> mockedCustomer = mockStatic(Customer.class);
                MockedStatic<Session> mockedSession = mockStatic(Session.class)) {

            String userEmail = "test@example.com";
            Set<Listing> listings = new HashSet<>();

            // Mock CustomerCollection
            CustomerCollection mockCustomerCollection = mock(CustomerCollection.class);
            Customer mockCustomer = mock(Customer.class);
            when(mockCustomer.getId()).thenReturn("test_customer_id");
            when(mockCustomer.getEmail()).thenReturn("test@example.com");

            List<Customer> customerList = Collections.singletonList(mockCustomer);
            when(mockCustomerCollection.getData()).thenReturn(customerList);

            // Mock Customer.list() to return the mock collection
            mockedCustomer.when(() -> Customer.list(any(CustomerListParams.class))).thenReturn(mockCustomerCollection);

            // Mock Customer.create()
            mockedCustomer.when(() -> Customer.create(any(CustomerCreateParams.class))).thenReturn(mockCustomer);

            // Mock Session.create()
            Session mockSession = mock(Session.class);
            when(mockSession.getUrl()).thenReturn("mock_session_url");
            mockedSession.when(() -> Session.create(any(SessionCreateParams.class))).thenReturn(mockSession);

            // Call the actual method
            String sessionUrl = paymentService.createPaymentSession(userEmail, listings);
            assertEquals("mock_session_url", sessionUrl);
        }
    }

    @Test
    public void testPaymentSuccess() throws StripeException {
        String sessionId = "valid_session_id";
        String userEmail = "user@example.com";
        String customerId = "customer_123";
        Long userId = 1L;

        Map<String, Set<Listing>> sessionListingsMap = new HashMap<>();

        try (MockedStatic<Session> mockedSession = mockStatic(Session.class);
                MockedStatic<Customer> mockedCustomer = mockStatic(Customer.class)) {

            Session mockSession = mock(Session.class);
            Customer mockCustomer = mock(Customer.class);

            when(mockSession.getPaymentStatus()).thenReturn("paid");
            when(mockSession.getCustomer()).thenReturn(customerId);
            when(mockCustomer.getEmail()).thenReturn(userEmail);

            mockedSession.when(() -> Session.retrieve(sessionId)).thenReturn(mockSession);
            mockedCustomer.when(() -> Customer.retrieve(customerId)).thenReturn(mockCustomer);

            User mockUser = new User();
            mockUser.setId(userId);
            mockUser.setEmail(userEmail);
            when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));

            Cart mockCart = mock(Cart.class);
            when(cartRepository.findById(userId)).thenReturn(Optional.of(mockCart));

            Listing mockListing = mock(Listing.class);
            when(mockListing.getListingID()).thenReturn(10L);
            Set<Listing> purchasedItems = new HashSet<>();
            purchasedItems.add(mockListing);
            sessionListingsMap.put(sessionId, purchasedItems);

            when(orderService.createOrder( anyLong())).thenReturn(new Order());

            String response = paymentService.paymentSuccess(sessionId);

            assertEquals("Payment successful! Orders have been created for user: " + userEmail, response);

            verify(orderService, times(1)).createOrder( eq(10L));

            verify(mockCart, times(1)).clearListings();
            verify(cartRepository, times(1)).save(mockCart);
        }
    }

    @Test
    public void testPaymentFailure() {
        String response = paymentService.paymentFailure();
        assertEquals("Payment cancelled. Try again.", response);
    }
}
