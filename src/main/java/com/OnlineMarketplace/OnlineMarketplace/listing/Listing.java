package com.OnlineMarketplace.OnlineMarketplace.listing;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="listing")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long listingID;

    @Enumerated(EnumType.STRING)
    private ListingType type;


    @Column(nullable = false)
    private String itemName;

    private String description;

    private double price;

    @Enumerated(EnumType.STRING)
    private PriceUnit unit;

    private String user;

    @Enumerated(EnumType.STRING)
    private Location location; //ArrayList to hold several Locations?

    private LocalDateTime startDate = LocalDateTime.now();

    private LocalDateTime endDate = startDate.plusMonths(2);

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

