package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.Review.Review;
import com.OnlineMarketplace.OnlineMarketplace.Review.ReviewController;
import com.OnlineMarketplace.OnlineMarketplace.Review.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
        objectMapper = new ObjectMapper(); // Initialize ObjectMapper for converting objects to JSON
    }

    @Test
    void testCreateReview() throws Exception {
        Review review = new Review();
        review.setId(1L);
        review.setReviewerName("John Doe");
        review.setContent("Great product!");
        review.setRating(5);

        when(reviewService.saveReview(any(Review.class))).thenReturn(review);

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review))) // Convert object to JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.reviewerName").value("John Doe"))
                .andExpect(jsonPath("$.content").value("Great product!"))
                .andExpect(jsonPath("$.rating").value(5));

        verify(reviewService).saveReview(any(Review.class));
    }

    @Test
    void testGetAllReviews() throws Exception {
        Review review1 = new Review();
        review1.setId(1L);
        review1.setReviewerName("Alice");
        review1.setContent("Nice quality");
        review1.setRating(4);

        Review review2 = new Review();
        review2.setId(2L);
        review2.setReviewerName("Bob");
        review2.setContent("Could be better");
        review2.setRating(3);

        when(reviewService.getAllReviews()).thenReturn(Arrays.asList(review1, review2));

        mockMvc.perform(get("/reviews")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].reviewerName").value("Alice"))
                .andExpect(jsonPath("$[1].reviewerName").value("Bob"));
    }

    @Test
    void testGetReviewById_Success() throws Exception {
        Review review = new Review();
        review.setId(1L);
        review.setReviewerName("Alice");
        review.setContent("Nice quality");
        review.setRating(4);

        when(reviewService.getReviewById(1L)).thenReturn(Optional.of(review));

        mockMvc.perform(get("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewerName").value("Alice"))
                .andExpect(jsonPath("$.content").value("Nice quality"))
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    void testGetReviewById_NotFound() throws Exception {
        when(reviewService.getReviewById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteReview() throws Exception {
        mockMvc.perform(delete("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(reviewService).deleteReview(eq(1L));
    }
}
