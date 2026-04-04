package com.saihoz.e_commerce_api.product.mapper;

import com.saihoz.e_commerce_api.product.Product;
import com.saihoz.e_commerce_api.product.dto.ProductRequestDTO;
import com.saihoz.e_commerce_api.product.dto.ProductResponseDTO;

public class ProductMapper {

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
        return dto;
    }
}