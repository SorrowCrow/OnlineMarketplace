package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.Bookmark.*;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserRepository;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import com.OnlineMarketplace.OnlineMarketplace.listing.ListingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    private User user;
    private Listing listing;
    private Bookmark bookmark;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        listing = new Listing();
        listing.setListingID(1L);
        listing.setTitle("Test Listing");

        bookmark = new Bookmark(user, listing);
        bookmark.setId(1L);
    }

    @Test
    void testGetUserBookmarks_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookmarkRepository.findByUser(user)).thenReturn(List.of(bookmark));

        List<BookmarkDTO> bookmarks = bookmarkService.getUserBookmarks(1L);

        assertFalse(bookmarks.isEmpty());
        assertEquals(1, bookmarks.size());
        assertEquals(listing.getListingID(), bookmarks.get(0).getListingId());
        assertEquals("Test Listing", bookmarks.get(0).getListingTitle());

        verify(userRepository, times(1)).findById(1L);
        verify(bookmarkRepository, times(1)).findByUser(user);
    }

    @Test
    void testAddBookmark_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        when(bookmarkRepository.findByUserAndListing(user, listing)).thenReturn(Optional.empty());

        Bookmark savedBookmark = new Bookmark(user, listing);
        savedBookmark.setId(1L);

        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(savedBookmark);

        BookmarkDTO savedBookmarkDTO = bookmarkService.addBookmark(1L, 1L);

        assertNotNull(savedBookmarkDTO);
        assertEquals(user.getId(), savedBookmarkDTO.getUserId());
        assertEquals(listing.getListingID(), savedBookmarkDTO.getListingId());
        assertEquals("Test Listing", savedBookmarkDTO.getListingTitle());

        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
    }

    @Test
    void testAddBookmark_FailsIfAlreadyExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        when(bookmarkRepository.findByUserAndListing(user, listing)).thenReturn(Optional.of(bookmark));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookmarkService.addBookmark(1L, 1L));

        assertEquals("Bookmark already exists", exception.getMessage());
        verify(bookmarkRepository, never()).save(any(Bookmark.class));
    }

    @Test
    void testRemoveBookmark_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        when(bookmarkRepository.findByUserAndListing(user, listing)).thenReturn(Optional.of(bookmark));

        BookmarkDTO removedBookmark = bookmarkService.removeBookmark(1L, 1L);

        assertNotNull(removedBookmark);
        assertEquals(user.getId(), removedBookmark.getUserId());
        assertEquals(listing.getListingID(), removedBookmark.getListingId());

        verify(bookmarkRepository, times(1)).delete(bookmark);
    }

    @Test
    void testRemoveBookmark_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        when(bookmarkRepository.findByUserAndListing(user, listing)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookmarkService.removeBookmark(1L, 1L));

        assertEquals("Bookmark not found", exception.getMessage());
        verify(bookmarkRepository, never()).delete(any(Bookmark.class));
    }
}
