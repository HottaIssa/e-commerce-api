package com.saihoz.e_commerce_api.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequestDTO {
    @NotBlank(message = "Shipping address")
    private String shippingAddress;
}
