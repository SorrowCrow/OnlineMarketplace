package com.OnlineMarketplace.OnlineMarketplace.listing;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//@Data
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

    private String description="default description";

    private double price = 10000;

    @Enumerated(EnumType.STRING)
    private PriceUnit unit;

    private String user;

    @Enumerated(EnumType.STRING)
    private Location location; //ArrayList to hold several Locations?

    @CreationTimestamp
    @Column(name = "start_date", updatable = false)
    private LocalDateTime startDate = LocalDateTime.now();

    @Column(name = "end_date")
    private LocalDateTime endDate = startDate.plusMonths(2);

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private long userID;
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private Category category;
//
//    public Listing() {
//        this.startDate = LocalDateTime.now();
//        this.endDate = startDate.plusMonths(1);
//    }

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

