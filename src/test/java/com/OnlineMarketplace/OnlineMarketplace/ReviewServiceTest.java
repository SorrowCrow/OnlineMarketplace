package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.entities.Review;
import com.OnlineMarketplace.OnlineMarketplace.repositories.ReviewRepository;
import com.OnlineMarketplace.OnlineMarketplace.services.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testSaveReview() {
        Review review = new Review();
        review.setId(1L);
        review.setReviewerName("John Smith");
        review.setContent("Great product!");
        review.setRating(5);

        when(reviewRepository.save(review)).thenReturn(review);

        Review savedReview = reviewService.saveReview(review);

        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getId()).isEqualTo(1L);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testGetAllReviews() {
        Review review1 = new Review();
        review1.setId(1L);
        Review review2 = new Review();
        review2.setId(2L);

        when(reviewRepository.findAll()).thenReturn(Arrays.asList(review1, review2));

        List<Review> reviews = reviewService.getAllReviews();

        assertThat(Optional.ofNullable(reviews)).hasSameClassAs(2);
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void testGetReviewById() {
        Review review = new Review();
        review.setId(1L);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        Optional<Review> foundReview = reviewService.getReviewById(1L);

        assertThat(foundReview).isPresent();
        assertThat(foundReview.get().getId()).isEqualTo(1L);
        verify(reviewRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteReview() {
        doNothing().when(reviewRepository).deleteById(1L);

        reviewService.deleteReview(1L);

        verify(reviewRepository, times(1)).deleteById(1L);
    }
}
