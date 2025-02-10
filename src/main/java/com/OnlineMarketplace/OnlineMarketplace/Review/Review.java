package com.OnlineMarketplace.OnlineMarketplace.Review;

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

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int rating; // Rating can be an integer from 1 to 5 (or any valid rating range)


}
