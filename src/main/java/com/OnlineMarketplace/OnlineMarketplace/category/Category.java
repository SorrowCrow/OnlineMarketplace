package com.OnlineMarketplace.OnlineMarketplace.category;

import com.OnlineMarketplace.OnlineMarketplace.listing.CategoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = false)
    CategoryType type;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;
}


