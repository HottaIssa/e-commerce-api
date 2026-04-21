package com.saihoz.e_commerce_api.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartItemRequest {
    @NotNull(message = "The quantity is required")
    @Min(value = 1, message = "The quantity must be at least 1")
    private Integer quantity;
}
