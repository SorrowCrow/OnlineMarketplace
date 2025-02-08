package com.OnlineMarketplace.OnlineMarketplace.listing;

import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.category.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="listings")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long listingID;

    @Enumerated(EnumType.STRING)
    private ListingType type;

    @Column(nullable = false)
    private String title;

    private String description;

    private double price;

    @Enumerated(EnumType.STRING)
    private PriceUnit unit;

    @Enumerated(EnumType.STRING)
    private Location location; //ArrayList to hold several Locations?

    @CreationTimestamp
    @Column(name = "start_date", updatable = false)
    private LocalDateTime startDate = LocalDateTime.now();

    @Column(name = "end_date")
    private LocalDateTime endDate = startDate.plusMonths(1);

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Listing))
            return false;

        Listing other = (Listing) o;

        return listingID == other.getListingID();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

