package com.OnlineMarketplace.OnlineMarketplace.Bookmark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDTO {
    private Long id;
    private Long userId;
    private Long listingId;
    private String listingTitle;
    private String listingDescription;
    private String listingPrice;
}
