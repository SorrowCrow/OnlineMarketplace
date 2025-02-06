package com.OnlineMarketplace.OnlineMarketplace.Cart;

import com.OnlineMarketplace.OnlineMarketplace.User.Model.User;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL, mappedBy = "cart")
    @JsonIgnore
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Column(name = "listing_id")
    private Set<Listing> listings = new HashSet<>();

    public void addListing(Listing listing){
        listings.add(listing);
    }

    public void removeListing(Listing listing){
        listings.removeIf(i-> i.getListingID()== listing.getListingID());
    }

    public void clearListings(){
        listings.clear();
    }

}
