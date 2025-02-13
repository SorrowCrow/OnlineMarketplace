package com.OnlineMarketplace.OnlineMarketplace.Payment;

import com.OnlineMarketplace.OnlineMarketplace.Cart.Cart;
import com.OnlineMarketplace.OnlineMarketplace.Cart.CartRepository;
import com.OnlineMarketplace.OnlineMarketplace.Order.Order;
import com.OnlineMarketplace.OnlineMarketplace.Order.OrderService;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserRepository;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import com.OnlineMarketplace.OnlineMarketplace.listing.ListingRepository;
import com.stripe.Stripe;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.model.tax.Registration.CountryOptions.Ca;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;

import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class PaymentService {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${client.base-url}")
    private String clientBaseURL;
    @Autowired
    private ListingRepository listingRepository;

    private final Map<String, Set<Listing>> sessionListingsMap = new HashMap<>();

    public String createPaymentSession(String userEmail, Set<Listing> listingList) throws StripeException {

        Stripe.apiKey = stripeSecretKey;

        Customer customer = null;
        CustomerListParams params = CustomerListParams.builder()
                .build();
        for (Customer c : Customer.list(params).getData()) {
            if (c.getEmail().equals(userEmail)) {
                customer = c;
                break;
            }
        }
        if (customer == null) {
            customer = Customer.create(
                    CustomerCreateParams.builder()
                            .setEmail(userEmail)
                            .build());
        }

        // Optional<Listing> productOptional = listingRepository.findById(listingId);
        // if (productOptional.isEmpty()) {
        // throw new IllegalArgumentException("Product with this ID was not found");
        // }
        // Listing listing = productOptional.get();

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomer(customer.getId())
                // .setCustomerEmail(userEmail)
                .setSuccessUrl(clientBaseURL + "/api/payments/success?session_id={CHECKOUT_SESSION_ID}")
                // .setSuccessUrl(clientBaseURL +
                // "/swagger-ui/index.html#operations-tag-payment-controller")
                .setCancelUrl(clientBaseURL + "/api/payments/failure");
        // .setCancelUrl(clientBaseURL +
        // "/swagger-ui/index.html#operations-tag-payment-controller");

        BigDecimal finalPrice = listingList.stream()
                .map(Listing::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(new BigDecimal("100"));

        log.info(finalPrice.toString());

        paramsBuilder.addLineItem(
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("eur")
                                        .setUnitAmountDecimal(finalPrice)
                                        .setProductData(
                                                PriceData.ProductData.builder()
                                                        // .putMetadata("listing_id",
                                                        // String.valueOf(listing.getListingID()))
                                                        // .setName(listing.getItemName())
                                                        .putMetadata("listing_id", "test")
                                                        .setName("test")
                                                        .build())
                                        .build())
                        .build());
        Session session = Session.create(paramsBuilder.build());
        sessionListingsMap.put(session.getId(), listingList);
        return session.getUrl();
    }

    public String paymentSuccess(String sessionId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        try {
            Session session = Session.retrieve(sessionId);
            String paymentStatus = session.getPaymentStatus();
            // String userEmail = session.getCustomerEmail();

            if (!"paid".equals(paymentStatus)) {
                return "Payment has not been finalized. Current Status: " + paymentStatus;
            }

            String customerId = session.getCustomer();
            if (customerId == null) {
                return "Payment successful, but customer ID is missing.";
            }
            Customer customer = Customer.retrieve(customerId);
            String userEmail = customer.getEmail();
            if (userEmail == null) {
                return "Payment successful, but user email is missing.";
            }

            Set<Listing> purchasedItems = sessionListingsMap.remove(sessionId);
            if (purchasedItems == null) {
                return "Payment successful, but no items found for session: 1" + sessionId;
            }

            Optional<User> userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                return "User not found for email: " + userEmail;
            }

            User buyer = userOptional.get();
            Optional<Cart> cartOptional = cartRepository.findById(buyer.getId());
            if (cartOptional.isEmpty()) {
                return "User's cart not found.";
            }

            Cart buyerCart = cartOptional.get();

            for (Listing listing : purchasedItems) {
                orderService.createOrder( listing.getListingID());
            }
            buyerCart.clearListings();
            cartRepository.save(buyerCart);

            return "Payment successful! Orders have been created for user: " + userEmail;

        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(
                    "Error during payment verification: " + e.getMessage(),
                    e.getRequestId(),
                    e.getCode(),
                    null,
                    null,
                    e);
        }
    }

    public String paymentFailure() {
        return "Payment cancelled. Try again.";
    }

}
