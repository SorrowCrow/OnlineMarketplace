package com.OnlineMarketplace.OnlineMarketplace.Cart;

import com.OnlineMarketplace.OnlineMarketplace.Auth.MessageResponse;
import com.OnlineMarketplace.OnlineMarketplace.Auth.SignUpRequestDTO;
import com.OnlineMarketplace.OnlineMarketplace.Security.JwtUtils;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserService;
import com.OnlineMarketplace.OnlineMarketplace.listing.Listing;
import com.OnlineMarketplace.OnlineMarketplace.listing.ListingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    JwtUtils jwtUtils;

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
     * Add Listing to Signed Up user's cart.
     *
     * @return List of all carts.
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addToCart/{listingId}")
    public ResponseEntity<?> addToCart(@PathVariable Long listingId, HttpServletRequest request) {
        String username = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));

        Optional<User> optionalUser = userService.findByEmail(username);

        if(optionalUser.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist"));
        }

        Optional<Listing> optionalListing = listingService.getListingById(listingId);

        if(optionalListing.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("No such listing"));
        }

        User user = optionalUser.get();

        user.getCart().addListing(optionalListing.get());

        user.getCart().addListing(optionalListing.get());

        cartService.save(user.getCart());

        return ResponseEntity.ok(cartService.findAll());
    }

    /**
     * Remove a Listing from signed in users cart.
     *
     * @return List of all carts.
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/remove/{listingId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long listingId, HttpServletRequest request) {
        String username = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));

        Optional<User> optionalUser = userService.findByEmail(username);

        if(optionalUser.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist"));
        }

        Optional<Listing> optionalListing = listingService.getListingById(listingId);

        if(optionalListing.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("No such listing"));
        }

        User user = optionalUser.get();

        user.getCart().addListing(optionalListing.get());

        cartService.save(user.getCart());

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

        SignUpRequestDTO request = new SignUpRequestDTO();

        request.setPassword("string");

        User user = userService.createUser(request);

        user.getCart().addListing(list1);

        cartService.save(user.getCart());

//        Listing fromDatabase = listingService.getListingById(user.getId());
//        user.getCart().removeListing(fromDatabase);
//        user = userService.updateUserCart(user);

//        userService.deleteUser(user.getId());

        return ResponseEntity.ok(cartService.findAll());
    }
}
