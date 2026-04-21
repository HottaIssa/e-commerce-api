package com.saihoz.e_commerce_api.order.dto;

import jakarta.validation.constraints.NotBlank;

public class OrderRequestDTO {
    @NotBlank(message = "Shipping address")
    private String shippingAddress;
}
