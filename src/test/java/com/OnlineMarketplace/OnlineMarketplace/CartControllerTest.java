package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.Cart.Cart;
import com.OnlineMarketplace.OnlineMarketplace.Cart.CartController;
import com.OnlineMarketplace.OnlineMarketplace.Cart.CartService;
import com.OnlineMarketplace.OnlineMarketplace.Security.JwtUtils;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserService;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import com.OnlineMarketplace.OnlineMarketplace.listing.ListingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @Mock
    private ListingService listingService;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void testGetAllCarts() throws Exception {
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddToCart() throws Exception {
        Long listingId = 1L;
        String fakeToken = "fakeToken";
        String userEmail = "test@example.com";

        User user = new User();
        user.setEmail(userEmail);
        user.setCart(new Cart());

        Listing listing = new Listing();
        listing.setListingID(listingId);

        when(jwtUtils.getJwtFromCookies(any())).thenReturn(fakeToken);
        when(jwtUtils.getUserNameFromJwtToken(fakeToken)).thenReturn(userEmail);
        when(userService.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(listingService.getListingById(listingId)).thenReturn(Optional.of(listing));

        mockMvc.perform(post("/api/cart/addToCart/{listingId}", listingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
