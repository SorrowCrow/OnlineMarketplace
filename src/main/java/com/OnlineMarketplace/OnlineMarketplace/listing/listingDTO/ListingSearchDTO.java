package com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO;

import com.OnlineMarketplace.OnlineMarketplace.listing.ListingType;
import com.OnlineMarketplace.OnlineMarketplace.listing.Location;
import com.OnlineMarketplace.OnlineMarketplace.listing.PriceUnit;

import jakarta.validation.constraints.Size;
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

    @Size(min = 2, max = 20, message = "Keyword must be between 2 and 20 characters long")
    private String keyword;

    @Size(min = 0, message = "Price must not be negative")
    private double minPrice;

    @Size(min = 0, message = "Price must not be negative")
    private double maxPrice;

    private PriceUnit unit;

    private Location location;

    private LocalDateTime minStartDate;

    private LocalDateTime maxStartDate;

    private LocalDateTime endDate;

    private Long categoryID;

}
