package com.OnlineMarketplace.OnlineMarketplace.listing;

import com.OnlineMarketplace.OnlineMarketplace.category.Category;
import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingCreateDTO;
import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingSearchDTO;
import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingUpdateDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/listings")
public class ListingController {

    @Autowired
    private ListingService listingService;

//ok//
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

    @GetMapping("/page")
    public Page<Listing> getAllListings(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size,
                                        @RequestParam(defaultValue = "listingID") String sortBy,
                                        @RequestParam(defaultValue = "true") boolean ascending) {

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return listingService.findAll(pageable);
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
    public List<Listing> getListingByPriceBetween(
            @RequestParam(
                    value = "minPrice",
                    required = false,
                    defaultValue = "0.0") BigDecimal minPrice,
            @RequestParam(
                    value = "maxPrice",
                    required = false) BigDecimal maxPrice) {
        return listingService.findByPriceBetween(minPrice, maxPrice);
    }

//ok//
    @GetMapping("/location")
    public List<Listing> getListingByLocation(@RequestParam Location location) {
        return listingService.findByLocation(location);
    }

//ok//
    @GetMapping("/category")
    public List<Listing> getListingByCategory(@RequestParam Category category) {
        return listingService.findByCategory(category);
    }

//ok//
    @GetMapping("/keyword")
    public List<Listing> getListingByKeyword(@RequestParam String keyword) {
        return listingService.searchByDescriptionKeyword(keyword);
    }

//ok//
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
    public ResponseEntity<?> deleteListing(@PathVariable("id") Long id) {  //204
        //listingService.deleteListing(id);
        //return ResponseEntity.noContent().build();
        try {
            listingService.deleteListing(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access Denied", "message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", e.getMessage()));
        }
    }


}