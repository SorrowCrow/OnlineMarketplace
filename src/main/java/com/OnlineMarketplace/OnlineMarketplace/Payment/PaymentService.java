package com.OnlineMarketplace.OnlineMarketplace.Payment;

import com.OnlineMarketplace.OnlineMarketplace.Cart.Cart;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import com.OnlineMarketplace.OnlineMarketplace.listing.ListingRepository;
import com.stripe.Stripe;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;


import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class PaymentService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${client.base-url}")
    private String clientBaseURL;
    @Autowired
    private ListingRepository listingRepository;

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
                            .build()
            );
        }

//        Optional<Listing> productOptional = listingRepository.findById(listingId);
//        if (productOptional.isEmpty()) {
//            throw new IllegalArgumentException("Product with this ID was not found");
//        }
//        Listing listing = productOptional.get();

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomer(customer.getId())
                .setSuccessUrl(clientBaseURL + "/api/payments/success?session_id={CHECKOUT_SESSION_ID}")
//                .setSuccessUrl(clientBaseURL + "/swagger-ui/index.html#operations-tag-payment-controller")
                .setCancelUrl(clientBaseURL + "/api/payments/failure");
//                .setCancelUrl(clientBaseURL + "/swagger-ui/index.html#operations-tag-payment-controller");

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
//                                                        .putMetadata("listing_id", String.valueOf(listing.getListingID()))
//                                                        .setName(listing.getItemName())
                                                        .putMetadata("listing_id", "test")
                                                        .setName("test")
                                                        .build()
                                        )
                                        .build()
                        )
                        .build()
        );
        Session session = Session.create(paramsBuilder.build());
        return session.getUrl();
    }

    public String paymentSuccess(String sessionId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        try {
            Session session = Session.retrieve(sessionId);
            String paymentStatus = session.getPaymentStatus();
            String userEmail = session.getCustomerEmail();

            if ("paid".equals(paymentStatus)) {
                return "Payment successful! Email: " + userEmail + ", status: " + paymentStatus;
            } else {
                return "Payment has not been finalized. Current Status: " + paymentStatus;
            }
        } catch (InvalidRequestException  e) {
            throw new InvalidRequestException(
                    "Error during payment verification: " + e.getMessage(),
                    e.getRequestId(),
                    e.getCode(),
                    null,
                    null,
                    e
            );
        }
    }

    public String paymentFailure() {
        return "Payment cancelled. Try again.";
    }

}
