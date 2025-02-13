package com.OnlineMarketplace.OnlineMarketplace.Bookmark;

import com.OnlineMarketplace.OnlineMarketplace.Security.JwtUtils;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<BookmarkDTO>> getUserBookmarks(HttpServletRequest request) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(bookmarkService.getUserBookmarks(user.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{listingId}")
    public ResponseEntity<BookmarkDTO> addBookmark(@PathVariable Long listingId, HttpServletRequest request) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(bookmarkService.addBookmark(user.getId(), listingId));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{listingId}")
    public ResponseEntity<Void> removeBookmark(@PathVariable Long listingId, HttpServletRequest request) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        bookmarkService.removeBookmark(user.getId(), listingId);
        return ResponseEntity.noContent().build();
    }
}