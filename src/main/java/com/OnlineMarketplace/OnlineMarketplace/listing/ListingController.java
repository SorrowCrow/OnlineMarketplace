package com.OnlineMarketplace.OnlineMarketplace.listing;

import com.OnlineMarketplace.OnlineMarketplace.category.Category;
import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingUpdateDTO;
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
    public ResponseEntity<Listing> getListingById(@PathVariable Long id) {
        Optional<Listing> listing = listingService.getListingById(id);
        return listing.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Listing> updateListing(@PathVariable Long id, @RequestParam Listing listingDetails) {
        try {
            Listing updatedListing = listingService.updateListing(id, listingDetails);
            return ResponseEntity.ok(updatedListing);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

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