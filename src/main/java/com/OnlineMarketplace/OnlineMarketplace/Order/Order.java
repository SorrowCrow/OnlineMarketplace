package com.OnlineMarketplace.OnlineMarketplace.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.category.Category;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import com.OnlineMarketplace.OnlineMarketplace.listing.ListingType;
import com.OnlineMarketplace.OnlineMarketplace.listing.Location;
import com.OnlineMarketplace.OnlineMarketplace.listing.PriceUnit;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "orders")
public class Order {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_buyer_id", nullable = false)
    private User buyer;
    
    private ListingType type;
    private String title;
    private String description;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private PriceUnit unit;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Enumerated(EnumType.STRING)
    private Location location;
    @ManyToOne
    @JoinColumn(name = "user_seller_id", nullable = false)
    private User seller;
    

    public Order() {}

    public Order(User buyer, Listing listing) {
        this.buyer = buyer;
        this.seller = listing.getUser(); 
        this.type = listing.getType();
        this.title = listing.getTitle();
        this.description = listing.getDescription();
        this.price = listing.getPrice();
        this.unit = listing.getUnit();
        this.startDate = listing.getStartDate();
        this.endDate = listing.getEndDate();
        this.category = listing.getCategory();
        this.location = listing.getLocation();

    
    }
}
