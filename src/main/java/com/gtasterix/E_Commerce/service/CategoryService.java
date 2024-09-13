package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.dto.CategoryDTO;
import com.gtasterix.E_Commerce.exception.CategoryNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.mapper.CategoryMapper;
import com.gtasterix.E_Commerce.model.Category;
import com.gtasterix.E_Commerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = CategoryMapper.toEntity(categoryDTO);
        validateCategory(category);
        return CategoryMapper.toDTO(categoryRepository.save(category));
    }

    public CategoryDTO getCategoryById(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryId + " not found"));
        return CategoryMapper.toDTO(category);
    }

    public CategoryDTO updateCategoryById(UUID categoryId, CategoryDTO categoryDTO) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category with ID " + categoryId + " not found");
        }
        Category category = CategoryMapper.toEntity(categoryDTO);
        category.setCategoryID(categoryId);
        return CategoryMapper.toDTO(categoryRepository.save(category));
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void validateCategory(Category category) {
        if (category.getCategoryName() == null || category.getCategoryName().isEmpty()) {
            throw new ValidationException("Category name cannot be null or empty");
        }
    }

    public void deleteCategoryById(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryId + " not found"));
        categoryRepository.delete(category);
    }


        public CategoryDTO patchCategoryById(UUID categoryId, CategoryDTO categoryDTO) {
            Category existingCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryId + " not found"));


            if (categoryDTO.getCategoryName() != null && !categoryDTO.getCategoryName().isEmpty()) {
                existingCategory.setCategoryName(categoryDTO.getCategoryName());
            }

            if (categoryDTO.getDescription() != null) {
                existingCategory.setDescription(categoryDTO.getDescription());
            }


            Category updatedCategory = categoryRepository.save(existingCategory);
            return CategoryMapper.toDTO(updatedCategory);
        }


    }


