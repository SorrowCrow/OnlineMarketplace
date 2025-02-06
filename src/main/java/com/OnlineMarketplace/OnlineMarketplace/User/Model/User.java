package com.OnlineMarketplace.OnlineMarketplace.User.Model;

import com.OnlineMarketplace.OnlineMarketplace.Cart.Cart;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String userSurname;
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Cart cart = new Cart();
}
