package com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO;

import com.OnlineMarketplace.OnlineMarketplace.listing.ListingType;
import com.OnlineMarketplace.OnlineMarketplace.listing.Location;
import com.OnlineMarketplace.OnlineMarketplace.listing.PriceUnit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class ListingUpdateDTO {

    private ListingType type;

    @Size(min = 2, max = 20, message = "Title must be between 2  and 20 characters long")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Size(min = 0, message = "Price must not be negative")
    private BigDecimal price;

    private PriceUnit priceUnit;

    private Location location;

    private Long categoryID;
}
