package com.saihoz.e_commerce_api.product.mapper;

import com.saihoz.e_commerce_api.product.Category;
import com.saihoz.e_commerce_api.product.dto.CategoryRequestDTO;
import com.saihoz.e_commerce_api.product.dto.CategoryResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }

    public CategoryResponseDTO toDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());

        return dto;
    }

    public List<CategoryResponseDTO> toDTOList(List<Category> categories) {
        return categories.stream().map(this::toDTO).toList();
    }
}