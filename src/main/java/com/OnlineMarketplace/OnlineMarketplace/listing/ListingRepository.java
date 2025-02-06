package com.OnlineMarketplace.OnlineMarketplace.listing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    List<Listing> findByType(ListingType type);
    List<Listing> findByPriceBetween(double minPrice, double maxPrice);
    List<Listing> findByLocation(Location location);
    @Query("SELECT l FROM Listing l WHERE LOWER(l.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Listing> searchByDescriptionKeyword(@Param("keyword") String keyword);
    List<Listing> findByStartDateBetween(LocalDateTime minStartDate, LocalDateTime maxStartDate);
    List<Listing> findByEndDate(LocalDateTime endDate);
    List<Listing> findByUser(String user);

}