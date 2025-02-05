package com.OnlineMarketplace.OnlineMarketplace.repositories;

import com.OnlineMarketplace.OnlineMarketplace.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
