package com.OnlineMarketplace.OnlineMarketplace.Cart;

import com.OnlineMarketplace.OnlineMarketplace.User.Model.User;
import com.OnlineMarketplace.OnlineMarketplace.User.Service.UserService;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import com.OnlineMarketplace.OnlineMarketplace.listing.ListingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    CartService cartService;


    @Autowired
    ListingService listingService;

    @Autowired
    UserService userService;

    /**
     * Get all carts.
     *
     * @return List of all carts.
     */
    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        return ResponseEntity.ok(cartService.findAll());
    }


    /**
     * Test cart, create a Listing and a User and assign listing to users cart.
     *
     * @return List of all carts.
     */
    @GetMapping("/testCart")
    public ResponseEntity<?> checkCarts() {
        Listing list1 = new Listing();
        list1.setDescription("test");
        list1.setPrice(100);
        list1.setItemName("Listing 1");

        User user = new User();

        user.getCart().addListing(list1);

        user = userService.createUser(user);

//        Listing fromDatabase = listingService.getListingById(user.getId());
//        user.getCart().removeListing(fromDatabase);
//        userService.createUser(user);

        return ResponseEntity.ok(cartService.findAll());
    }
}
