/*
one item per listing?
listings also for searching items?
listing location - enum? ArrayList? should be possible to have several entries
include delivery type? connect to delivery/mailbox services?
link to userID or User? (itemID or Item?)

anybody used Statemachine?
 */


package com.OnlineMarketplace.OnlineMarketplace.listing;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

public enum ListingType {
    SELL, BUY, FOR_RENT, TO_RENT; //assuming also listings for searching items
}

public enum PriceUnit {
    PIECE, HOUR, DAY, WEEK, MONTH;
}

public enum Location {
    RIGA, VILNIUS, ANY; //TODO: need a separate Location entity?
}

@Entity
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")

    @Getter
    private long listingID;

    @Getter @Setter
    private ListingType type;

    @Getter @Setter
    private double price;

    @Getter @Setter
    private PriceUnit priceUnit;

    @Getter @Setter
    private ArrayList<Location> location;

    @Getter @Setter
    private String description;

    @Getter
    @Column(name = "startDate");
    private LocalDate startDate;

    @Getter @Setter
    @Column(name = "endDate")
    private LocalDate endDate; //TODO: predefined listing time - x days/weeks from UI?

    @Getter @Setter
    private boolean isActive; //possibility to pause a listing (reserve item)

    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private long userID;

    @Getter
    @OneToOne
    @JoinColumn(name = "item_id")
    private long itemID; //TODO: each item has a separate listing or allow several items per listing?

    //TODO: other overloaded constructors needed?
    public Listing() {
        this.startDate = LocalDateTime.now();
        this.isActive = true;
    }

}

