package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.category.Category;
import com.OnlineMarketplace.OnlineMarketplace.listing.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListingServiceTest {

    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private ListingService listingService;

    private Listing testListing;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
        //type, title, description, price, unit, location, user, category

    @Test
    void deleteListing_validId_deletesListing() {
        when(listingRepository.findById(1L)).thenReturn(Optional.of(testListing)); // Mock the repository
        doNothing().when(listingRepository).delete(testListing);

        listingService.deleteListing(1L);

        verify(listingRepository, times(1)).delete(testListing); // Verify delete was called
    }

/*
    @Test
    void deleteListing_listingNotFound_throwsException() {
        when(listingRepository.findById(1L)).thenReturn(Optional.empty()); // Mock empty result

        assertThrows(ListingNotFoundException.class, () -> listingService.deleteListing(1L));

        verify(listingRepository, never()).delete(any()); // Verify delete was NOT called
    }
*/

    // ... more test methods for other scenarios (e.g., database errors)
}