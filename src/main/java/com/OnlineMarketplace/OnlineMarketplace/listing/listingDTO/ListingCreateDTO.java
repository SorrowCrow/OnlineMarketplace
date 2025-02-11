package com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO;

import com.OnlineMarketplace.OnlineMarketplace.listing.ListingType;
import com.OnlineMarketplace.OnlineMarketplace.listing.Location;
import com.OnlineMarketplace.OnlineMarketplace.listing.PriceUnit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingCreateDTO {

    @NotNull(message = "Listing type is required")
    private ListingType type;

    @NotNull (message = "Title must be provided")
    @Size(min = 2, max = 20, message = "Title must be between 2  and 20 characters long")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Price must be provided")
    @Size(min = 0, message = "Price must not be negative")
    private BigDecimal price;

    @NotNull (message = "Unit for the price must be provided")
    private PriceUnit priceUnit;

    @NotNull (message = "Location must be provided")
    private Location location;

    @NotNull
    private Long userID;

    @NotNull
    private Long categoryID;
}
