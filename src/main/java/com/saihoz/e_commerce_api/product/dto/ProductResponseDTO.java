package com.saihoz.e_commerce_api.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductResponseDTO {

    private UUID id;
    private String name;
    private double price;
    private int stock;
    private String description;
    private String image;

    private List<String> categories;

}