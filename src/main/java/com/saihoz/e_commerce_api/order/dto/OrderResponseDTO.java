package com.saihoz.e_commerce_api.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderResponseDTO {
    private UUID id;
    private String status;
    private double totalAmount;
    private String shippingAddress;
    private List<OrderItemResponseDTO> items;
    private LocalDateTime createdAt;
}
