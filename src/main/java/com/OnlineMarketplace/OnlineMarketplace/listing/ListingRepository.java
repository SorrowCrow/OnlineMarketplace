package com.OnlineMarketplace.OnlineMarketplace.listing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Optional, but good practice

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

}