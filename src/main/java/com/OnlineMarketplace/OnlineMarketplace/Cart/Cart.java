package com.OnlineMarketplace.OnlineMarketplace.Cart;

import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Accessors(chain = true)
@Table
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @JoinTable
    @OneToMany
//    @ManyToMany(fetch = FetchType.LAZY
//            ,
//            cascade = {
////            CascadeType.ALL
////                    CascadeType.PERSIST,
////                    CascadeType.MERGE
//                    CascadeType.DETACH
//            })
//    @JoinTable(name = "cart_listings",
//            joinColumns = {@JoinColumn(name = "cart_id")},
//            inverseJoinColumns = {@JoinColumn(name = "listing_id")})
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Listing> listings = new HashSet<>();

    public void addListing(Listing listing) {
        listings.add(listing);
    }

    public void removeListing(Listing listing) {
        listings.removeIf(i -> i.getListingID() == listing.getListingID());
    }

    public void clearListings() {
        listings.clear();
    }

}
