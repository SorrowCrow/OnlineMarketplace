package com.OnlineMarketplace.OnlineMarketplace.listing;

import com.OnlineMarketplace.OnlineMarketplace.category.Category;
import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingCreateDTO;
import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingUpdateDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

//need to check!//
    @PostMapping
    public ResponseEntity<Listing> createListing(@Valid @RequestBody ListingCreateDTO listingCreateDTO) {
        try {
            Listing createdListing = listingService.createListing(listingCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdListing); // 201 Created
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

//ok//
    @GetMapping
    public List<Listing> getAllListings() {
        return listingService.getAllListings();
    }

//need to check!//
    @GetMapping("/user/{userID}")
    public List<Listing> getListingsByUserId(@PathVariable Long userID) {
        return listingService.findByUserId(userID);
    }

//ok//
    @GetMapping("/type")
    public List<Listing> getListingByType(@RequestParam ListingType type) {
        return listingService.findByType(type);
    }

//ok//
    @GetMapping("/price")
    public List<Listing> getListingByPriceBetween( //NOT WORKING!!
            @RequestParam(
                    value = "minPrice",
                    required = false,
                    defaultValue = "0.0") double minPrice,
            @RequestParam(
                    value = "maxPrice",
                    required = false) double maxPrice) {
        return listingService.findByPriceBetween(minPrice, maxPrice);
    }

/*
    @GetMapping
    public List<Listing> getListingByStartDateBetween(
            @RequestParam LocalDateTime minStartDate,
            @RequestParam LocalDateTime maxStartDate) {
        return listingService.findByStartDateBetween(minStartDate, maxStartDate);
    }
*/

//ok//
    @GetMapping("/location")
    public List<Listing> getListingByLocation(@RequestParam Location location) {
        return listingService.findByLocation(location);
    }

//need to check!//
    @GetMapping("/{id}")
    public ResponseEntity<Listing> getListingById(@PathVariable Long id) {
        Optional<Listing> listing = listingService.getListingById(id);
        return listing.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//need to check!//
    @PatchMapping("/{id}")
    public ResponseEntity<Listing> updateListing(@PathVariable Long id, @Valid @RequestBody ListingUpdateDTO listingUpdateDTO) {
        try {
            Listing updatedListing = listingService.updateListing(id, listingUpdateDTO);
            return ResponseEntity.ok(updatedListing);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
//ok//
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable Long id) {  //204
        listingService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/create")
////    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<?> createListing(@RequestBody Listing listing){
//        return ResponseEntity.ok(listingService.createListing(listing));
//    }


}