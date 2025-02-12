package com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingPaginationDTO {
    int page = 0;
    int size = 5;
    String sortBy = "listingID";
    boolean ascending = true;
}
