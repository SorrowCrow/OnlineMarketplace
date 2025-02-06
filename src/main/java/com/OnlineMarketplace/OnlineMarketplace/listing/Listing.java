package com.OnlineMarketplace.OnlineMarketplace.listing;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Entity
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long listingID;

    @Setter
    private ListingType type;

    @Setter
    private double price;

    @Setter
    private PriceUnit priceUnit;

    @Setter
    private Location location; //ArrayList to hold several Locations?

    @Setter
    private String description;

    private LocalDateTime startDate;

    @Setter
    private LocalDateTime endDate;

    @Setter
    private boolean isActive = true;

/*
    @ManyToOne
    @JoinColumn(name = "user_id")
    private long userID;

    @OneToOne
    @JoinColumn(name = "item_id")
    private long itemID;
*/

//    private String user;

    @Setter
    private String itemName;

    public Listing() {
        this.startDate = LocalDateTime.now();
        this.endDate = startDate.plusMonths(1);
    }

}

