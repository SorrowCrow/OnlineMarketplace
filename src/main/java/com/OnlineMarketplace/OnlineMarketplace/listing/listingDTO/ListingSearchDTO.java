package com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO;

import com.OnlineMarketplace.OnlineMarketplace.listing.ListingType;
import com.OnlineMarketplace.OnlineMarketplace.listing.Location;
import com.OnlineMarketplace.OnlineMarketplace.listing.PriceUnit;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ListingSearchDTO {

    private Optional<ListingType> type;

/*
    @Size(min = 2, max = 20, message = "Keyword must be between 2 and 20 characters long")
    private Optional<String> keyword;
*/

    @Size(min = 0, message = "Price must not be negative")
    private Optional<Double> minPrice;

    @Size(min = 0, message = "Price must not be negative")
    private Optional<Double> maxPrice;

    private Optional<PriceUnit> unit;

    private Optional<Location> location;

//    private LocalDateTime minStartDate;

//    private LocalDateTime maxStartDate;

//    private LocalDateTime endDate;

//    private Long categoryID;

}
