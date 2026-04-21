package com.saihoz.e_commerce_api.cart.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CartResponse {
    private UUID cartId;
    private List<CartItemResponse> items;
    private Integer totalItems;
    private double total;
}
