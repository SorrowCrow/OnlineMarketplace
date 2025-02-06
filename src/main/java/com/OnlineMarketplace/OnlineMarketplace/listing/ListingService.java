package com.OnlineMarketplace.OnlineMarketplace.listing;

import com.OnlineMarketplace.OnlineMarketplace.category.Category;
import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingUpdateDTO;
import jakarta.persistence.EntityNotFoundException;
import com.OnlineMarketplace.OnlineMarketplace.Cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ListingService {
    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private CartService cartService;

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public List<Listing> findByType(ListingType type) {
        return listingRepository.findByType(type);
    }

    public List<Listing> findByPriceBetween(double minPrice, double maxPrice) {
        return listingRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Listing> findByStartDateBetween(LocalDateTime minStartDate, LocalDateTime maxStartDate) {
        return listingRepository.findByStartDateBetween(minStartDate, maxStartDate);
    }

    public List<Listing> findByLocation(Location location) {
        return listingRepository.findByLocation(location);
    }

    public Optional<Listing> getListingById(Long id) {
        return listingRepository.findById(id);
    }

//    public List<Listing> searchByDescriptionKeyword(@Param("keyword") String keyword);

    public Listing createListing(Listing listing) {
        return listingRepository.save(listing);
    }

//todo: learn how to PATCH with DTO
/*
    public Listing updateListing(long id, ListingUpdateDTO listingUpdateDTO) {
    }
*/
public Listing updateListing(Long id, Listing listingDetails) {
    return listingRepository.findById(id)
            .map(listing -> {
                listing.setType(listingDetails.getType());
                listing.setItemName(listingDetails.getItemName());
                listing.setDescription(listingDetails.getDescription());
                listing.setPrice(listingDetails.getPrice());
                listing.setUnit(listingDetails.getUnit());
                listing.setLocation(listingDetails.getLocation());
                listing.setEndDate(listingDetails.getEndDate());
                return listingRepository.save(listing);
            }).orElseThrow(() -> new RuntimeException("Listing not found"));
}

    public void deleteListing(Long id) {
        Optional<Listing> optionalListing = getListingById(id);
        if(optionalListing.isEmpty()){
            return;
        }
        cartService.deleteListingFromCarts(optionalListing.get());
        listingRepository.deleteById(id);
    }

}