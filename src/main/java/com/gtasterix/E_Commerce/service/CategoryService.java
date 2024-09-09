package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.exception.CategoryNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.model.Category;
import com.gtasterix.E_Commerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        validateCategory(category);
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create category", e);
        }
    }

    public Category getCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryId + " not found"));
    }

    public Category updateCategoryById(UUID categoryId, Category category) {
        validateCategory(category);
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category with ID " + categoryId + " not found");
        }
        category.setCategoryID(categoryId);
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update category with ID " + categoryId, e);
        }
    }

    public List<Category> getAllCategories() {
        try {
            return categoryRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve all categories", e);
        }
    }

    public Category getCategoryByName(String name) {
        return categoryRepository.findByCategoryName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category with name " + name + " not found"));
    }

    private void validateCategory(Category category) {
        if (category.getCategoryName() == null || category.getCategoryName().isEmpty()) {
            throw new ValidationException("Category name cannot be null or empty");
        }
    }

    public Category patchCategoryById(UUID categoryId, Map<String, Object> updates) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryId + " not found"));

        if (updates.containsKey("name")) {
            String name = (String) updates.get("name");
            if (name == null || name.isEmpty()) {
                throw new ValidationException("Category name cannot be null or empty");
            }
            existingCategory.setCategoryName(name);
        }
        if (updates.containsKey("description")) {
            existingCategory.setDescription((String) updates.get("description"));
        }

        try {
            return categoryRepository.save(existingCategory);
        } catch (Exception e) {
            throw new RuntimeException("Failed to partially update category with ID " + categoryId, e);
        }
    }

    public void deleteCategoryById(UUID categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryId + " not found"));

        try {
            categoryRepository.delete(existingCategory);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete category with ID " + categoryId, e);
        }
    }
}
