package com.saihoz.e_commerce_api.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderItemResponseDTO {
    private UUID id;
    private UUID productId;
    private String productName;
    private String productImage;
    private Integer quantity;
    private double unitPrice;
    private double subtotal;
}
