package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.Cart.CartService;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserRepository;
import com.OnlineMarketplace.OnlineMarketplace.User.UserService;
import com.OnlineMarketplace.OnlineMarketplace.category.Category;
import com.OnlineMarketplace.OnlineMarketplace.category.CategoryRepository;
import com.OnlineMarketplace.OnlineMarketplace.category.CategoryService;
import com.OnlineMarketplace.OnlineMarketplace.listing.*;
import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingCreateDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.annotations.CreationTimestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListingServiceTest {

    @Mock
    private ListingRepository listingRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CartService cartService;

    @InjectMocks
    private ListingService listingService;

    @Test
    void testCreateListing_validInput_createsListingWitAutoFilledDates() {
        User mockUser = new User();
        mockUser.setId(1L);

        Category mockCategory = new Category();
        mockCategory.setId(1L);

        ListingCreateDTO listingCreateDTO = new ListingCreateDTO();
        listingCreateDTO.setType(ListingType.SELL);
        listingCreateDTO.setTitle("Test Listing");
        listingCreateDTO.setDescription("A test listing description.");
        listingCreateDTO.setPrice(new BigDecimal("99.99"));
        listingCreateDTO.setPriceUnit(PriceUnit.PIECE);
        listingCreateDTO.setLocation(Location.RIGA);
        listingCreateDTO.setUserID(1L);
        listingCreateDTO.setCategoryID(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));

        when(listingRepository.save(any(Listing.class))).thenAnswer(invocation -> {
            Listing listing = invocation.getArgument(0);
            assertNotNull(listing.getStartDate());
            assertTrue(listing.getStartDate().isBefore(LocalDateTime.now().plusSeconds(1)));
            assertNotNull(listing.getEndDate());
            assertEquals(listing.getStartDate().plusMonths(1), listing.getEndDate());
            assertEquals(ListingType.SELL, listing.getType());
            assertEquals(1L, listing.getCategory().getId());
            assertEquals("99.99", listing.getPrice().toString());
            assertEquals(PriceUnit.PIECE, listing.getUnit());
            assertEquals("Test Listing", listing.getTitle());
            assertEquals("A test listing description.", listing.getDescription());
            assertEquals(1L, listing.getUser().getId());
            assertEquals(1l, listing.getCategory().getId());

            return listing;
        });

        Listing createdListing = listingService.createListing((listingCreateDTO));
        assertNotNull(createdListing);

        verify(listingRepository, times(1)).save(any(Listing.class));
    }
}