package com.OnlineMarketplace.OnlineMarketplace.listing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/listing")
public class ListingController {

    @Autowired
    private ListingService listingService;

    @PostMapping("/add")
    public Listing createListing(@RequestBody Listing listing) { //should use ListingCreateDTO
        return listingService.createListing(listing);
    }

    @GetMapping("/search/results")
    public List<Listing> getAllListings() {
        return listingService.getAllListings();
    }

    @GetMapping("/search/results")
    public List<Listing> getListingByType(ListingType type) {
        return listingService.findByType(type);
    }

    @GetMapping("/search/results")
    public List<Listing> getListingByPriceBetween(double minPrice, double maxPrice) {
        return listingService.findByPriceBetween(minPrice, maxPrice);
    }

    @GetMapping("/search/results")
    public List<Listing> getListingByStartDateBetween(LocalDateTime minStartDate, LocalDateTime maxStartDate) {
        return listingService.findByStartDateBetween(minStartDate, maxStartDate);
    }

    @GetMapping("/search/results")
    public List<Listing> getListingByLocation(Location location) {
        return listingService.findByLocation(location);
    }

    @GetMapping("/{id}")
    public Listing getListingById(long id) {
        return listingService.getListingById(id);
    }
    /*
    @PatchMapping
    public Listing updateListingPrice(@RequestParam double price) {
    }
*/

    @DeleteMapping
    public void deleteListing(long listingID) {
        listingService.deleteListing(listingID);
    }

}