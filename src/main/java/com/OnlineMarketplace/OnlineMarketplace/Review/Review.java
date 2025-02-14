package com.OnlineMarketplace.OnlineMarketplace.Review;

import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "reviews")
@Data // Lombok annotation to automatically generate getters, setters, and other methods like toString, equals, and hashCode
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate the ID
    private Long id;

    @Column(nullable = false) // Ensures the column cannot be null in the database
    private String reviewerName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    @Column()
    private String content;

    @Column(nullable = false)
    private int rating; // Rating can be an integer from 1 to 5 (or any valid rating range)

    public Review() {}

    public Review(User user, Listing listing, String content, int rating) {
        this.user = user;
        this.listing = listing;
        this.content = content;
        this.rating = rating;
        this.reviewerName = user.getName();
    }


}
