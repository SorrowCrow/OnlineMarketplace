package com.OnlineMarketplace.OnlineMarketplace.listing;

import com.OnlineMarketplace.OnlineMarketplace.Cart.Cart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="listings")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long listingID;

    @Enumerated(EnumType.STRING)
    private ListingType type;

    @Column(nullable = false)
    private String itemName = "default name";

    private String description="default description";

    private double price = 10000;

    @Enumerated(EnumType.STRING)
    private PriceUnit unit;

    private String user;

    @Enumerated(EnumType.STRING)
    private Location location; //ArrayList to hold several Locations?

    private LocalDateTime startDate = LocalDateTime.now();

    private LocalDateTime endDate = startDate.plusMonths(2);

//    @ManyToMany(fetch = FetchType.LAZY,
//            cascade = {
////                    CascadeType.ALL,
//                    CascadeType.DETACH
//            },
//            mappedBy = "listings")
//    @JsonIgnore
////    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Set<Cart> listings = new HashSet<>();

    /*
    @Setter
    private boolean isActive;
*/

/*
    @ManyToOne
    @JoinColumn(name = "user_id")
    private long userID;

    @OneToOne
    @JoinColumn(name = "item_id")
    private long itemID;
*/
}

