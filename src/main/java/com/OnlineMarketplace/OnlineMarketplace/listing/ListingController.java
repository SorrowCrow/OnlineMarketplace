package com.OnlineMarketplace.OnlineMarketplace.listing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/listings")
public class ListingController {

    @Autowired
    private ListingService listingService;

//todo: use ListingCreateDTO
/*
    @PostMapping
    public Listing createListing(@RequestBody Listing listing) {
        return listingService.createListing(listing);
    }
*/



    @GetMapping
    public List<Listing> getAllListings() {
        return listingService.getAllListings();
    }


    @GetMapping("/type")
    public List<Listing> getListingByType(@RequestParam ListingType type) {
        return listingService.findByType(type);
    }

    @GetMapping("/price")
    public List<Listing> getListingByPriceBetween(
            @RequestParam(
                    value = "minPrice",
                    required = false,
                    defaultValue = "0.0") double minPrice,
            @RequestParam(
                    value = "minPrice",
                    required = false,
                    defaultValue = "1000000") double maxPrice) {
        return listingService.findByPriceBetween(minPrice, maxPrice);
    }

//todo: formatting for LocalDateTime
/*
    @GetMapping
    public List<Listing> getListingByStartDateBetween(
            @RequestParam LocalDateTime minStartDate,
            @RequestParam LocalDateTime maxStartDate) {
        return listingService.findByStartDateBetween(minStartDate, maxStartDate);
    }
*/

    @GetMapping("/location")
    public List<Listing> getListingByLocation(@RequestParam Location location) {
        return listingService.findByLocation(location);
    }

    @GetMapping("/{id}")
    public Optional<Listing> getListingById(@PathVariable long id) {
        return listingService.getListingById(id);
    }

//todo: use ListingUpdateDTO
/*
    @PatchMapping
    public Listing updateListingPrice(@RequestParam double price) {
    }
*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable Long id) {
        listingService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create")
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createListing(@RequestBody Listing listing){
        return ResponseEntity.ok(listingService.createListing(listing));
    }


}