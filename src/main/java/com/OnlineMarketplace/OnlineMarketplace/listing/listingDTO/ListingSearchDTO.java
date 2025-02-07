package com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO;

import com.OnlineMarketplace.OnlineMarketplace.listing.ListingType;
import com.OnlineMarketplace.OnlineMarketplace.listing.Location;
import com.OnlineMarketplace.OnlineMarketplace.listing.PriceUnit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingSearchDTO {
    private ListingType type;
    private String itemName;
    private String description;
    private double minPrice;
    private double maxPrice;
    private PriceUnit unit;
    private Location location;
    private LocalDateTime minStartDate;
    private LocalDateTime maxStartDate;
    private LocalDateTime endDate;
//this is a test//
}
