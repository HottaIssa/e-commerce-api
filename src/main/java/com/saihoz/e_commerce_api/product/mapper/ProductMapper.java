package com.saihoz.e_commerce_api.product.mapper;

import com.saihoz.e_commerce_api.product.Category;
import com.saihoz.e_commerce_api.product.Product;
import com.saihoz.e_commerce_api.product.dto.ProductRequestDTO;
import com.saihoz.e_commerce_api.product.dto.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    @Autowired
    private CategoryMapper categoryMapper;

    public Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setDescription(dto.getDescription());
        product.setImage(dto.getImage());
        return product;
    }

    public ProductResponseDTO toDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setDescription(product.getDescription());
        dto.setImage(product.getImage());
        dto.setCategories(
                categoryMapper.toDTOList(product.getCategories())
        );
        return dto;
    }

    public List<ProductResponseDTO> toDTOList(List<Product> products) {
        return products.stream().map(this::toDTO).toList();
    }
}