package com.OnlineMarketplace.OnlineMarketplace.listing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ListingService {
    @Autowired
    private ListingRepository listingRepository;
    private ListingRepository list;

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public List<Listing> findByType(ListingType type) {
        return listingRepository.findByType(type);
    }

    public List<Listing> findByPriceBetween(double minPrice, double maxPrice) {
        return listingRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Listing> findByLocation(Location location) {
        return listingRepository.findByLocation(location);
    }

    public Listing getListingById(Long id) {
        return listingRepository.findById(id).orElse(null);
    }

    public Listing createListing(Listing listing) {
        return listingRepository.save(listing);
    }

//todo: learn how to PATCH with DTO
/*
    public Listing updateListing(Listing listing) {
        return listingRepository.save(listing);
    }
*/

    public void deleteListing(Long id) {
        listingRepository.deleteById(id);
    }

}