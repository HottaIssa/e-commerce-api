package com.saihoz.e_commerce_api.cart.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CartItemResponse {
    private UUID cartItemId;
    private UUID productId;
    private String productName;
    private String productImage;
    private double unitPrice;
    private Integer quantity;
    private double subtotal;
}
