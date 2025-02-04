/*
one item per listing?
listings also for searching items?
listing location - enum? ArrayList? should be possible to have several entries
include delivery type? connect to delivery/mailbox services?
link to userID or User? (itemID or Item?)

anybody used Lombok?
anybody used Statemachine?
 */


package com.OnlineMarketplace.OnlineMarketplace.listing;

import java.time.LocalDateTime;

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
    private long listingID;

    private ListingType type;

    private double price;

    private PriceUnit priceUnit;

    private ArrayList<Location> location;

    @Column(name = "startDate");

    @Column(name = "endDate")
    private LocalDate endDate; //TODO: predefined listing time - x days/weeks from UI?

    private boolean isActive; //possibility to pause a listing (reserve item)

    @ManyToOne
    @JoinColumn(name = "user_id")
    private long userID;

    @OneToOne
    @JoinColumn(name = "item_id")
    private long itemID; //TODO: each item has a separate listing or allow several items per listing?

    //TODO: other overloaded constructors needed?
    public Listing() {
        this.startDate = LocalDateTime.now();
        this.isActive = true;
    }

    //GETTERS for all fields

    public long getListingID() {
        return listingID;
    }

    public ListingType getListingType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public PriceUnit getPriceUnit() {
        return priceUnit;
    }

    public Location getLocation() {
        return location;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public long getUserID() {
        return userID;
    }

    public long getItemID() {
        return itemID;
    }

    //SETTERS for type, price, priceUnit, location, endDate, isActive

    public void setType(ListingType type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPriceUnit(PriceUnit priceUnit) {
        this.priceUnit = priceUnit;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

