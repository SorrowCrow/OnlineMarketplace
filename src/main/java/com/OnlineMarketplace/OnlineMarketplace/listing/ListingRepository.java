package com.OnlineMarketplace.OnlineMarketplace.listing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    List<Listing> findByType(ListingType type);
    List<Listing> findByPriceBetween(double minPrice, double maxPrice);
    List<Listing> findByLocation(Location location);
//    @Query("SELECT l FROM Listing l WHERE LOWER(l.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    List<Listing> searchByDescriptionKeyword(@Param("keyword") String keyword);
    List<Listing> findByStartDateBetween(LocalDateTime minStartDate, LocalDateTime maxStartDate);
//    List<Listing> findByEndDateBetween(LocalDateTime minEndDate, LocalDateTime maxEndDate);
//    List<Listing> findByIsActive(boolean isActive);
//    List<Listing> findByUserID(Long userID);
//    List<Listing> findByItemID(Long itemID);


//    List<Listing> findByUserIDAndTypeOrderByCreationDateAsc(Long userID, ListingType type);
//    List<Listing> findByUserIDAndTypeOrderByEndDateAsc(Long userID, ListingType type);
//    List<Listing> findByLocationAndType(Location location, ListingType type);


}