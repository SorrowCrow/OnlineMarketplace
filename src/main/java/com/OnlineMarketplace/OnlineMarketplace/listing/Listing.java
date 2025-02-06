package com.OnlineMarketplace.OnlineMarketplace.listing;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Entity
@AllArgsConstructor
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long listingID;

    @Setter
    @Enumerated(EnumType.STRING)
    private ListingType type;

    @Setter
    @Column(nullable = false)
    private String itemName;

    @Setter
    private String description;

    @Setter
    private double price;

    @Setter
    @Enumerated(EnumType.STRING)
    private PriceUnit unit;

    @Setter
    @Enumerated(EnumType.STRING)
    private Location location; //ArrayList to hold several Locations?

    private LocalDateTime startDate;

    @Setter
    private LocalDateTime endDate;

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

    private String user;

    public Listing() {
        this.startDate = LocalDateTime.now();
        this.endDate = startDate.plusMonths(1);
//        this.isActive = true;
    }

}

