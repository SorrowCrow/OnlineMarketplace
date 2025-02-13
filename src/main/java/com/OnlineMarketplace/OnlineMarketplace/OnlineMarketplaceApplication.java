package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.Cart.CartService;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserRepository;
import com.OnlineMarketplace.OnlineMarketplace.Wordlist.Wordlist;
import com.OnlineMarketplace.OnlineMarketplace.category.Category;
import com.OnlineMarketplace.OnlineMarketplace.category.CategoryService;
import com.OnlineMarketplace.OnlineMarketplace.listing.*;
import com.OnlineMarketplace.OnlineMarketplace.listing.listingDTO.ListingCreateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Slf4j
@SpringBootApplication
public class OnlineMarketplaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineMarketplaceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(ListingService listingService, CategoryService categoryService, UserRepository userRepository, PasswordEncoder passwordEncoder, CartService cartService) {
        return (args) -> {
            // Populate database
            User user = new User("andrejs@andrejs.com", "Andrejs", "Matvejevs", "andrejs");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setAccountVerified(true);
            user.setVerificationToken(null);
            user.setVerificationTokenExpiry(null);
            user = userRepository.save(user);

            Category category = categoryService.createCategory(new Category().setName("default name").setDescription("default description").setType(CategoryType.ELECTRONICS));
            log.info(Wordlist.productDescriptions.size() + "");
            log.info(Wordlist.productTitles.size() + "");
            log.info(Wordlist.productPrices.size() + "");
            for (int i = 0; i < Wordlist.productTitles.size(); i++) {
                Listing listing = listingService.createListing(new ListingCreateDTO(ListingType.SELL, Wordlist.productTitles.get(i), Wordlist.productDescriptions.get(i), BigDecimal.valueOf(Wordlist.productPrices.get(i)), PriceUnit.PIECE, Location.RIGA, user.getId(), category.getId()));
                if (i < 5) {
                    user.getCart().addListing(listing);
                }
            }
            cartService.save(user.getCart());

        };
    }
}
