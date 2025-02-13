package com.OnlineMarketplace.OnlineMarketplace.User;

import com.OnlineMarketplace.OnlineMarketplace.Bookmark.Bookmark;
import com.OnlineMarketplace.OnlineMarketplace.Cart.Cart;
import com.OnlineMarketplace.OnlineMarketplace.Role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String email;

    @JsonIgnore
    private String password;

    private boolean accountVerified;

    @Column(nullable = true, unique = true)
    @JsonIgnore
    private String verificationToken;

    @Column(nullable = true)
    @JsonIgnore
    private LocalDateTime verificationTokenExpiry;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Cart cart = new Cart();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bookmark> bookmarks = new HashSet<>();


    public User(String email,String name, String surname, String password) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
    }


}
