package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.category.Category;
import com.OnlineMarketplace.OnlineMarketplace.category.CategoryController;
import com.OnlineMarketplace.OnlineMarketplace.category.CategoryService;
import com.OnlineMarketplace.OnlineMarketplace.listing.CategoryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void testGetAllCategories() throws Exception {
        Category category1 = new Category(1L, CategoryType.valueOf("ELECTRONICS"), "Devices", "Various devices");
        Category category2 = new Category(2L, CategoryType.valueOf("CLOTHING"),"Fashion", "Clothing and accessories");

        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(category1, category2));

        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Devices"))
                .andExpect(jsonPath("$[1].name").value("Fashion"));
    }

    @Test
    void testGetCategoryById_Success() throws Exception {
        Category category = new Category(1L, CategoryType.valueOf("ELECTRONICS"), "Devices", "Various devices");

        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category));

        mockMvc.perform(get("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Devices"));
    }

    @Test
    void testGetCategoryById_NotFound() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCategory() throws Exception {
        Category category = new Category(1L, CategoryType.valueOf("ELECTRONICS"), "Devices", "Various devices");

        when(categoryService.createCategory((Category) any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Devices\",\"description\":\"Various devices\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Devices"));
    }


    @Test
    void testUpdateCategory_Success() throws Exception {
        Category updatedCategory = new Category(1L, CategoryType.valueOf("ELECTRONICS"), "Updated Devices", "Updated description");

        when(categoryService.updateCategory(eq(1L), (Category) any(Category.class))).thenReturn(updatedCategory);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Devices\",\"description\":\"Updated description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Devices"));
    }


    @Test
    void testUpdateCategory_NotFound() throws Exception {
        when(categoryService.updateCategory(1L, new Category())).thenThrow(new RuntimeException("Category not found"));

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Non-existent\",\"description\":\"Non-existent\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}