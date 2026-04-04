package com.saihoz.e_commerce_api.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CategoryResponseDTO {

    private UUID id;
    private String name;
    private String description;

    private List<UUID> productIds;

}