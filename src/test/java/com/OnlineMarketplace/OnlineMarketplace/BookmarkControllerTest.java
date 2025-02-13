package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.Bookmark.*;
import com.OnlineMarketplace.OnlineMarketplace.Security.JwtUtils;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookmarkControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookmarkService bookmarkService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookmarkController bookmarkController;

    private User testUser;
    private BookmarkDTO testBookmarkDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookmarkController).build();

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        testBookmarkDTO = new BookmarkDTO(1L, testUser.getId(), 1L, null, null, null);

        when(jwtUtils.getJwtFromCookies(any(HttpServletRequest.class))).thenReturn("mocked-token");
        when(jwtUtils.getUserNameFromJwtToken("mocked-token")).thenReturn(testUser.getEmail());
        when(userService.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
    }

    /** ✅ Test GET /api/bookmarks */
    @Test
    void testGetUserBookmarks_Success() throws Exception {
        when(bookmarkService.getUserBookmarks(testUser.getId())).thenReturn(Collections.singletonList(testBookmarkDTO));

        mockMvc.perform(get("/api/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testBookmarkDTO.getId()))
                .andExpect(jsonPath("$[0].userId").value(testBookmarkDTO.getUserId()))
                .andExpect(jsonPath("$[0].listingId").value(testBookmarkDTO.getListingId()));

        verify(bookmarkService, times(1)).getUserBookmarks(testUser.getId());
    }

    /** ✅ Test POST /api/bookmarks/{listingId} */
    @Test
    void testAddBookmark_Success() throws Exception {
        when(bookmarkService.addBookmark(eq(testUser.getId()), eq(1L))).thenReturn(testBookmarkDTO);

        mockMvc.perform(post("/api/bookmarks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testBookmarkDTO.getId()))
                .andExpect(jsonPath("$.userId").value(testBookmarkDTO.getUserId()))
                .andExpect(jsonPath("$.listingId").value(testBookmarkDTO.getListingId()));

        verify(bookmarkService, times(1)).addBookmark(testUser.getId(), 1L);
    }

    /** ✅ FIXED: Test DELETE /api/bookmarks/{listingId} */
    @Test
    void testRemoveBookmark_Success() throws Exception {
        // FIX: Instead of doNothing(), return a mock response
        when(bookmarkService.removeBookmark(eq(testUser.getId()), eq(1L))).thenReturn(testBookmarkDTO);

        mockMvc.perform(delete("/api/bookmarks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(bookmarkService, times(1)).removeBookmark(testUser.getId(), 1L);
    }


}
