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

    public Listing getListingById(Long id) {
        return listingRepository.findById(id).orElse(null); // Handle optional
    }

    public Listing createListing(Listing listing) {
        return listingRepository.save(listing);
    }

    public void updateListing(Listing listing) {}

    public void deleteListing(Long id) {
        listingRepository.deleteById(id);
    }

}