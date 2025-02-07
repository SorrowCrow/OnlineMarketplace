package com.OnlineMarketplace.OnlineMarketplace.Cart;

import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByListingsListingID(Long id);
}

