package com.OnlineMarketplace.OnlineMarketplace.Review;

import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository; // Using 'final' for immutability

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findReviewsByUser(user);
    }

    public List<Review> getReviewsByListing(Listing listing) {
        return reviewRepository.findReviewsByListing(listing);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}