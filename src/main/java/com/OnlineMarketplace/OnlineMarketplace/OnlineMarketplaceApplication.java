package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.Auth.SignUpRequestDTO;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserService;
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

import java.math.BigDecimal;

@Slf4j
@SpringBootApplication
public class OnlineMarketplaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineMarketplaceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(UserService userService, ListingService listingService, CategoryService categoryService) {
        return (args) -> {
            // Populate database
            User user = userService.createUser(new SignUpRequestDTO("andrejs@andrejs.com", "andrejs", "Andrejs", "Matvejevs"));
            Category category = categoryService.createCategory(new Category().setName("default name").setDescription("default description").setType(CategoryType.ELECTRONICS));
            log.info(Wordlist.productDescriptions.size()+"");
            log.info(Wordlist.productTitles.size()+"");
            log.info(Wordlist.productPrices.size()+"");
            for (int i = 0; i < Wordlist.productTitles.size(); i++) {
                listingService.createListing(new ListingCreateDTO(ListingType.SELL, Wordlist.productTitles.get(i), Wordlist.productDescriptions.get(i), BigDecimal.valueOf(Wordlist.productPrices.get(i)), PriceUnit.PIECE, Location.RIGA, user.getId(), category.getId()));
            }
        };
    }
}
