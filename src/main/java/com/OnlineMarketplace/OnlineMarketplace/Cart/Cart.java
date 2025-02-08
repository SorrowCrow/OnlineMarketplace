package com.OnlineMarketplace.OnlineMarketplace.Cart;

import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Entity
@Data
@Accessors(chain = true)
@Table
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cart_listings",
            joinColumns = {@JoinColumn(name = "cart_id")},
            inverseJoinColumns = {@JoinColumn(name = "listing_id")})
    private Set<Listing> listings = new HashSet<>();

    public void addListing(Listing listing) {
        this.listings.add(listing);
    }

    public void removeListing(Long listingId) {
        Listing listing = this.listings.stream().filter(l -> l.getListingID() == listingId).findFirst().orElse(null);
        log.info(listingId.toString() + (listing !=null));

        if(listing !=null){
            this.listings.remove(listing);
        }
    }

    public void clearListings() {
        listings.clear();
    }

}
