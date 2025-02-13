package com.OnlineMarketplace.OnlineMarketplace.User;

import com.OnlineMarketplace.OnlineMarketplace.Auth.LoginResponseDTO;
import com.OnlineMarketplace.OnlineMarketplace.Auth.MessageResponse;
import com.OnlineMarketplace.OnlineMarketplace.Auth.UserDTO;
import com.OnlineMarketplace.OnlineMarketplace.Security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<?> getUser(HttpServletRequest request) {
        String username = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));

        Optional<User> optionalUser = userService.findByEmail(username);

        if(optionalUser.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist"));
        }

        User user = optionalUser.get();

        return ResponseEntity.ok(user);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDetailsDTO userUpdateDTO, HttpServletRequest request) {
        String username = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));

        Optional<User> optionalUser = userService.findByEmail(username);

        if(optionalUser.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist"));
        }

        User user = optionalUser.get();

        if (!user.getEmail().equals(userUpdateDTO.getEmail()) &&  userService.existsByEmail(userUpdateDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
        }

        User newuser = userService.updateUserDetails(userUpdateDTO, user);

        if(!user.getEmail().equals(userUpdateDTO.getEmail())){
            ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body( new MessageResponse("You've been signed out!"));
        }

        return ResponseEntity.ok(newuser);
    }

//    @PreAuthorize("#id == authentication.principal.email")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


//    @PreAuthorize("#id == authentication.principal.email")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }


//    @PreAuthorize("#id == authentication.principal.email")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.badRequest().body("User deletion failed");
    }
}