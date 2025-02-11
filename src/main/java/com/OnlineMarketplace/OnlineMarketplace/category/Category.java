package com.OnlineMarketplace.OnlineMarketplace.category;

import com.OnlineMarketplace.OnlineMarketplace.listing.CategoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    CategoryType categoryType;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;
}


