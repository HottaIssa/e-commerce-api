package com.saihoz.e_commerce_api.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductRequestDTO {

    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    @Min(0)
    private Double price;

    @NotNull
    @Min(0)
    private Integer stock;

    private String description;
    private String image;

    private List<UUID> categoryIds;

}