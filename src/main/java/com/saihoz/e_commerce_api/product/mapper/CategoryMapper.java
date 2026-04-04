package com.saihoz.e_commerce_api.product.mapper;

import com.saihoz.e_commerce_api.product.Category;
import com.saihoz.e_commerce_api.product.Product;
import com.saihoz.e_commerce_api.product.dto.CategoryRequestDTO;
import com.saihoz.e_commerce_api.product.dto.CategoryResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        if (category.getProduct() != null) {
            List<UUID> productIds = category.getProduct()
                    .stream()
                    .map(Product::getId)
                    .collect(Collectors.toList());

            dto.setProductIds(productIds);
        }

        return dto;
    }
}