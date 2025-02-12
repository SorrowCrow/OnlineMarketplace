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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@SpringBootApplication
public class OnlineMarketplaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineMarketplaceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(ListingService listingService, CategoryService categoryService, UserRepository userRepository, PasswordEncoder passwordEncoder, CartService cartService) {
        return (args) -> {

            List<String> names = new ArrayList<>(List.of("Andrejs", "Anton", "Ruslan", "Lelde", "Ieva"));
            List<String> surnames = new ArrayList<>(List.of("Matvejevs", "Den", "Dzhubuev", "Brosova", "Krigere"));

            for (int i = 0; i < names.size(); i++) {
                User user = new User(names.get(i).toLowerCase() + "@" + names.get(i).toLowerCase() + ".com", names.get(i), surnames.get(i), names.get(i).toLowerCase());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setAccountVerified(true);
                user.setVerificationToken(null);
                user.setVerificationTokenExpiry(null);
                user = userRepository.save(user);
            }

            String[][] categories = {
                {"EMPLOYMENT", "Vacancy", "Job offers"},
                {"EMPLOYMENT", "Training course", "All types of courses, including bootcamps"},
                {"CONSTRUCTION", "Construction material", "All kinds of materials: from bricks to lumber to roof tiles"},
                {"CONSTRUCTION", "Service", "From architecture to demolition"},
                {"HOBBY", "Book", "From fiction to dictionary"},
                {"HOBBY", "Fishing", "Fishing equipment, company etc."},
                {"TRANSPORT", "Car", "for moving people"},
                {"TRANSPORT", "Truck", "for moving goods"},
                {"REAL_ESTATE", "House", "Detatched and semi-detatched habitable housing units"},
                {"REAL_ESTATE", "Apartment", "Single-family housing units in multi-apartment buildings"},
                {"CLOTHING", "Footware", "All you can wear on your feet"},
                {"CLOTHING", "Accessory", "From bags to scarves to hats"},
                {"ANIMALS", "Pet", "from fish to cat to horse"},
                {"ANIMALS", "Pet food", "food for pets"},
                {"ELECTRONICS", "TV", "TV sets of all shapes and sizes"},
                {"ELECTRONICS", "Computer", "From laptops to gaming machines"},
                {"FOR_HOME", "Home appliance", "Hairdriers, microwave ovens, washing machines etc."},
                {"FOR_HOME", "Furniture", "Tables, chairs, wardrobes etc."},
                {"MANUFACTURING", "Manufacturing material", "Production materials"},
                {"MANUFACTURING", "Machinery", "Industrial manufacturing equipment"},
                {"AGRICULTURE", "Equipment", "Tractors, harvesters etc."},
                {"AGRICULTURE", "Chemical", "Fertilisers etc."},
                {"OTHER", "other", "Everything else not listed in other categories"}
            };

/*
            for (int i = 0; i < categories.length; i++) {
                String type = categories[i][0];
                String name = categories[i][1];
                String description = categories[i][2];
                Category category = categoryService.createCategory(new Category().setName(name).setDescription(description).setType(CategoryType.valueOf(type)));
            }
*/

            Category category = categoryService.createCategory(new Category().setName("Category name").setDescription("Category description").setType(CategoryType.valueOf("ELECTRONICS")));


            List<User> users = userRepository.findAll();
//            List<Category> allCategories = categoryService.getAllCategories();
            log.info(Wordlist.productDescriptions.size() + "");
            log.info(Wordlist.productTitles.size() + "");
            log.info(Wordlist.productPrices.size() + "");
            for (int i = 0; i < Wordlist.productTitles.size(); i++) {
                Collections.shuffle(users);
//                Collections.shuffle(allCategories);
                User user = users.get(0);
//                Category category = allCategories.get(0);
                listingService.createListing(new ListingCreateDTO(ListingType.SELL, Wordlist.productTitles.get(i), Wordlist.productDescriptions.get(i), BigDecimal.valueOf(Wordlist.productPrices.get(i)), PriceUnit.PIECE, Location.RIGA, user.getId(), category.getId()));
            }
        };
    }
}
