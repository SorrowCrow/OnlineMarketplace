package com.OnlineMarketplace.OnlineMarketplace.listing;

import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingSearchDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    @PersistenceContext
    EntityManager entityManager;

    List<Listing> findByType(ListingType type);

    List<Listing> findByPriceBetween(double minPrice, double maxPrice);

    List<Listing> findByLocation(Location location);

    @Query("SELECT l FROM Listing l WHERE LOWER(l.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(l.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Listing> searchByDescriptionKeyword(@Param("keyword") String keyword);

//    List<Listing> findByStartDateBetween(LocalDateTime minStartDate, LocalDateTime maxStartDate);

//    List<Listing> findByEndDate(LocalDateTime endDate);

    default List<Listing> searchListings(ListingSearchDTO listingSearchDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Listing> cq = cb.createQuery(Listing.class);
        Root<Listing> root = cq.from(Listing.class);
        List<Predicate> predicates = new ArrayList<>();
//type; keyword; minPrice; maxPrice; unit; location; minStartDate; maxStartDate; endDate; categoryID;
        listingSearchDTO.getType().ifPresent(type -> predicates.add(cb.like(root.get("type"), "%" + type + "%")));
//        ListingSearchDTO.getDescription().ifPresent(description -> predicates.add(cb.like(root.get("description"), "%" + description + "%")));

        listingSearchDTO.getMinPrice().ifPresent(minPrice -> predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice)));
        listingSearchDTO.getMaxPrice().ifPresent(maxPrice -> predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice)));
        listingSearchDTO.getUnit().ifPresent(unit -> predicates.add(cb.like(root.get("unit"), "%" + unit + "%")));

//        ListingSearchDTO.getCategory().ifPresent(category -> predicates.add(cb.equal(root.get("category"), category)));
        listingSearchDTO.getLocation().ifPresent(location -> predicates.add(cb.like(root.get("location"), "%" + location + "%")));

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();

    }

    List<Listing> findByUserId(Long userID);

}

