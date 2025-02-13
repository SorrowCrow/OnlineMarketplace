package com.OnlineMarketplace.OnlineMarketplace.Bookmark;

import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserRepository;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import com.OnlineMarketplace.OnlineMarketplace.listing.ListingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListingRepository listingRepository;

    public List<BookmarkDTO> getUserBookmarks(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return bookmarkRepository.findByUser(user).stream()
                .map(bookmark -> new BookmarkDTO(
                        bookmark.getId(),
                        user.getId(),
                        bookmark.getListing().getListingID(),
                        bookmark.getListing().getTitle(),
                        bookmark.getListing().getDescription(),
                        bookmark.getListing().getPrice().toString()
                )).collect(Collectors.toList());
    }

    public BookmarkDTO addBookmark(Long userId, Long listingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new EntityNotFoundException("Listing not found"));

        if (bookmarkRepository.findByUserAndListing(user, listing).isPresent()) {
            throw new IllegalArgumentException("Bookmark already exists");
        }

        Bookmark bookmark = new Bookmark(user, listing);
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        return new BookmarkDTO(
                savedBookmark.getId(),
                user.getId(),
                listing.getListingID(),
                listing.getTitle(),
                listing.getDescription(),
                listing.getPrice().toString()
        );
    }

    public BookmarkDTO removeBookmark(Long userId, Long listingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new EntityNotFoundException("Listing not found"));

        Optional<Bookmark> bookmark = bookmarkRepository.findByUserAndListing(user, listing);
        if (bookmark.isPresent()) {
            Bookmark removedBookmark = bookmark.get();


            System.out.println("Deleting Bookmark ID: " + removedBookmark.getId());
            System.out.println("User: " + user.getEmail() + " | Listing: " + listing.getTitle());

            bookmarkRepository.delete(removedBookmark);

            return new BookmarkDTO(
                    removedBookmark.getId(),
                    user.getId(),
                    listing.getListingID(),
                    listing.getTitle(),
                    listing.getDescription(),
                    listing.getPrice().toString()
            );
        } else {
            System.out.println("Bookmark not found for User ID: " + userId + " and Listing ID: " + listingId);
            throw new EntityNotFoundException("Bookmark not found");
        }
    }
}
