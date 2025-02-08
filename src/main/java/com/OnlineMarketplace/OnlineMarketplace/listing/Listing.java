package com.OnlineMarketplace.OnlineMarketplace.listing;

import com.OnlineMarketplace.OnlineMarketplace.Cart.Cart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
    private String itemName = "default name";

    private String description="default description";

    private double price = 10000;

    @Enumerated(EnumType.STRING)
    private PriceUnit unit;

    private String user;

    @Enumerated(EnumType.STRING)
    private Location location; //ArrayList to hold several Locations?

    private LocalDateTime startDate = LocalDateTime.now();

    private LocalDateTime endDate = startDate.plusMonths(2);
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

