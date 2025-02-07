package com.OnlineMarketplace.OnlineMarketplace.listing;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserRepository;
import com.OnlineMarketplace.OnlineMarketplace.category.Category;
import com.OnlineMarketplace.OnlineMarketplace.category.CategoryRepository;
import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingCreateDTO;
import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingUpdateDTO;
import jakarta.persistence.CascadeType;
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
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartService cartService;

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public List<Listing> findByUserId(Long userID) {
        return listingRepository.findByUserId(userID);
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

/*
    public Listing createListing(ListingCreateDTO listingCreateDTO) {
        Listing listing = new Listing();

        listing.setType(listingCreateDTO.getType());
        listing.setDescription(listingCreateDTO.getDescription());
        listing.setPrice(listingCreateDTO.getPrice());
        listing.setUnit(listingCreateDTO.getPriceUnit());
        listing.setLocation(listingCreateDTO.getLocation());
        listing.setEndDate(listingCreateDTO.getEndDate());

        User user = userRepository.findById(listingCreateDTO.getUserID())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Category category = categoryRepository.findById(listingCreateDTO.getCategoryID())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        listing.setUser(user);
        listing.setCategory(category);

        return listingRepository.save(listing);
    }
*/

/*
public Listing updateListing(Long id, Listing listingDetails) {
    Listing listing = listingRepository.findById(id)
            .map(listing -> {
                listing.setType(listingDetails.getType());
                listing.setTitle(listingDetails.getTitle());
                listing.setDescription(listingDetails.getDescription());
                listing.setPrice(listingDetails.getPrice());
                listing.setUnit(listingDetails.getUnit());
                listing.setLocation(listingDetails.getLocation());
                listing.setEndDate(listingDetails.getEndDate());
                return listingRepository.save(listing);
            }).orElseThrow(() -> new RuntimeException("Listing not found"));
}
*/

    public Listing updateListing(Long id, ListingUpdateDTO listingUpdateDTO) {
        Optional<Listing> existingListing = listingRepository.findById(id);

        if (existingListing.isEmpty()) {
            throw new EntityNotFoundException("Listing not found");
        }

        Listing listing = existingListing.get();
        if (listingUpdateDTO.getType() != null) {
            listing.setType(listingUpdateDTO.getType());
        }

        if (listingUpdateDTO.getTitle() != null) {
            listing.setTitle(listingUpdateDTO.getTitle());
        }

        if (listingUpdateDTO.getDescription() != null) {
            listing.setDescription(listingUpdateDTO.getDescription());
        }

        listing.setPrice(listingUpdateDTO.getPrice());
        if (listingUpdateDTO.getPriceUnit() != null) {
            listing.setUnit(listingUpdateDTO.getPriceUnit());
        }

        if (listingUpdateDTO.getEndDate() != null) {
            listing.setEndDate(listingUpdateDTO.getEndDate());
        }

        if (listingUpdateDTO.getCategoryID() != null) {
            Category newCategory = categoryRepository.findById(listingUpdateDTO.getCategoryID())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            listing.setCategory(newCategory);
        }
        return listingRepository.save(listing);

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