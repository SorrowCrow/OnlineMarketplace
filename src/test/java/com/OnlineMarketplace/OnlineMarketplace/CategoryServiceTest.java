package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.category.Category;
import com.OnlineMarketplace.OnlineMarketplace.category.CategoryRepository;
import com.OnlineMarketplace.OnlineMarketplace.category.CategoryService;
import com.OnlineMarketplace.OnlineMarketplace.listing.CategoryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        Category category1 = new Category(1L, CategoryType.valueOf("ELECTRONICS"), "Devices", "Various devices");
        Category category2 = new Category(2L, CategoryType.valueOf("HOBBY"),"Paintings", "Various paintings");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<Category> categories = categoryService.getAllCategories();

        assertThat(categories).hasSize(2);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryById() {
        Category category = new Category(1L, CategoryType.valueOf("ELECTRONICS"), "Devices", "Various devices");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> foundCategory = categoryService.getCategoryById(1L);

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Devices");
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateCategory() {
        Category category = new Category(null, CategoryType.valueOf("CLOTHING"),"Fashion", "Clothing and accessories");

        when(categoryRepository.save(category)).thenReturn(new Category(1L, CategoryType.valueOf("CLOTHING"), "Fashion","Clothing and accessories"));

        Category savedCategory = categoryService.createCategory(category);

        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isEqualTo(1L);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testUpdateCategory() {
        Category existingCategory = new Category(1L, CategoryType.valueOf("ELECTRONICS"), "Devices", "Various devices");
        Category updatedDetails = new Category(1L, CategoryType.valueOf("ELECTRONICS"), "Tech", "Updated description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedDetails);

        Category updatedCategory = categoryService.updateCategory(1L, updatedDetails);

        assertThat(updatedCategory.getName()).isEqualTo("Tech");
        assertThat(updatedCategory.getDescription()).isEqualTo("Updated description");
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(existingCategory);
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
