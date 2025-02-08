package com.OnlineMarketplace.OnlineMarketplace.Cart;

import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    /**
     * Find all Carts.
     *
     * @return A List containing Carts if found, otherwise empty.
     */
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    /**
     * Get a cart by its ID.
     *
     * @param id The ID of the cart.
     * @return An Optional containing the cart if found, otherwise empty.
     */
    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }

    /**
     * Save a cart.
     *
     * @param cart The cart to be saved.
     * @return Saved cart.
     */
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    public void deleteListingFromCarts(Listing listing){
        List<Cart> carts = cartRepository.findByListingsListingID(listing.getListingID());
        carts.forEach(i-> {
            i.removeListing(listing.getListingID());
            cartRepository.save(i);
        });
    }
}
