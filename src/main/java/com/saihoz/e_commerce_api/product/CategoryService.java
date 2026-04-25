package com.saihoz.e_commerce_api.product;

import com.saihoz.e_commerce_api.exception.CategoryNotFoundException;
import com.saihoz.e_commerce_api.product.dto.CategoryRequestDTO;
import com.saihoz.e_commerce_api.product.dto.CategoryResponseDTO;
import com.saihoz.e_commerce_api.product.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryMapper.toDTOList(categoryRepository.findAll());
    }

    public CategoryResponseDTO getCategoryById(Long id) {
        return categoryMapper.toDTO(categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found")));
    }

    public CategoryResponseDTO saveCategory(CategoryRequestDTO request) {

        Category category = categoryMapper.toEntity(request);

        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        Category category = categoryMapper.toEntity(request);
        existingCategory.setName(category.getName());
        return categoryMapper.toDTO(categoryRepository.save(existingCategory));
    }

    public void deleteCategory(Long id) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        categoryRepository.deleteById(id);
    }
}
