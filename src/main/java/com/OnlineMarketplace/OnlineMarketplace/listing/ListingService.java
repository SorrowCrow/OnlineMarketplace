package com.OnlineMarketplace.OnlineMarketplace.listing;

import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserRepository;
import com.OnlineMarketplace.OnlineMarketplace.category.Category;
import com.OnlineMarketplace.OnlineMarketplace.category.CategoryRepository;
import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingCreateDTO;
import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingSearchDTO;
import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingUpdateDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EntityNotFoundException;
import com.OnlineMarketplace.OnlineMarketplace.Cart.CartService;
import com.OnlineMarketplace.OnlineMarketplace.Role.ERole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public Page<Listing> findAll(Pageable pageable) {
        return listingRepository.findAll(pageable);
    }

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public List<Listing> findByUserId(Long userID) {
        return listingRepository.findByUserId(userID);
    }

    public List<Listing> findByType(ListingType type) {
        return listingRepository.findByType(type);
    }

    public List<Listing> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return listingRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Listing> findByLocation(Location location) {
        return listingRepository.findByLocation(location);
    }

    public List<Listing> findByCategory(Category category) {
        return listingRepository.findByCategory(category);
    }

    public Optional<Listing> getListingById(Long id) {
        return listingRepository.findById(id);
    }

    public List<Listing> searchByDescriptionKeyword(@Param("keyword") String keyword) {
        return listingRepository.searchByDescriptionKeyword(keyword);
    }

    public Listing createListing(ListingCreateDTO listingCreateDTO) {
        Listing listing = new Listing();

        listing.setType(listingCreateDTO.getType());
        listing.setTitle(listingCreateDTO.getTitle());
        listing.setDescription(listingCreateDTO.getDescription());
        listing.setPrice(listingCreateDTO.getPrice());
        listing.setUnit(listingCreateDTO.getPriceUnit());
        listing.setLocation(listingCreateDTO.getLocation());
        listing.setStartDate(LocalDateTime.now());
        listing.setEndDate(LocalDateTime.now().plusMonths(1));

        User user = userRepository.findById(listingCreateDTO.getUserID())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        listing.setUser(user);

        Category category = categoryRepository.findById(listingCreateDTO.getCategoryID())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        listing.setCategory(category);

        return listingRepository.save(listing);
    }

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
        if (listingUpdateDTO.getCategoryID() != null) {
            Category newCategory = categoryRepository.findById(listingUpdateDTO.getCategoryID())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            listing.setCategory(newCategory);
        }
        return listingRepository.save(listing);
    }

    public void deleteListing(Long id) {
        Optional<Listing> optionalListing = getListingById(id);
        if (optionalListing.isEmpty()) {
            return;
        }
        // cartService.deleteListingFromCarts(optionalListing.get());
        // listingRepository.deleteById(id);
        Listing listing = optionalListing.get();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) {
            throw new AccessDeniedException("Unauthorized access");
        }
        String email = ((UserDetails) principal).getUsername(); 
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean isOwner = listing.getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals(ERole.ROLE_ADMIN));

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You are not authorized to delete this listing.");
        }

        cartService.deleteListingFromCarts(listing);
        listingRepository.deleteById(id);
    }
}