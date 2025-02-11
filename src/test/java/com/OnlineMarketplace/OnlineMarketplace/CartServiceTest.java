package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.Cart.Cart;
import com.OnlineMarketplace.OnlineMarketplace.Cart.CartRepository;
import com.OnlineMarketplace.OnlineMarketplace.Cart.CartService;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    private Cart cart;
    private Listing listing;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        cart.setId(1L);

        listing = new Listing();
        listing.setListingID(1L);
    }

    @Test
    void testFindAll_ReturnsListOfCarts() {
        when(cartRepository.findAll()).thenReturn(List.of(cart));
        List<Cart> carts = cartService.findAll();
        assertEquals(1, carts.size());
        verify(cartRepository, times(1)).findAll();
    }

    @Test
    void testFindById_WhenCartExists() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        Optional<Cart> result = cartService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testFindById_WhenCartDoesNotExist() {
        when(cartRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Cart> result = cartService.findById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testSaveCart() {
        when(cartRepository.save(cart)).thenReturn(cart);
        Cart savedCart = cartService.save(cart);
        assertNotNull(savedCart);
        assertEquals(1L, savedCart.getId());
    }

    @Test
    void testDeleteListingFromCarts() {
        when(cartRepository.findByListingsListingID(1L)).thenReturn(List.of(cart));
        cartService.deleteListingFromCarts(listing);
        assertTrue(cart.getListings().isEmpty());
        verify(cartRepository, times(1)).save(cart);
    }
}
