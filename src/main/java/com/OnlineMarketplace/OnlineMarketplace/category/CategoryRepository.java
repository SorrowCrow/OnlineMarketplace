package com.OnlineMarketplace.OnlineMarketplace.category;

import com.OnlineMarketplace.OnlineMarketplace.listing.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

List<Category> findByType(CategoryType type);
}

