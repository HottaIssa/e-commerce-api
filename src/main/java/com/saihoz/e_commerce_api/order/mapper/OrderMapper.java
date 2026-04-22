package com.saihoz.e_commerce_api.order.mapper;

import com.saihoz.e_commerce_api.order.Order;
import com.saihoz.e_commerce_api.order.dto.OrderItemResponseDTO;
import com.saihoz.e_commerce_api.order.dto.OrderResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    public OrderResponseDTO mapToResponse(Order order) {
        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(order.getId());
        response.setStatus(order.getStatus().name());
        response.setTotalAmount(order.getTotalAmount());
        response.setShippingAddress(order.getShippingAddress());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponseDTO> itemResponses = order.getItems().stream().map(item -> {
            OrderItemResponseDTO itemResponse = new OrderItemResponseDTO();
            itemResponse.setId(item.getId());
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setProductImage(item.getProduct().getImage());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setUnitPrice(item.getUnitPrice());
            itemResponse.setSubtotal(item.getUnitPrice() * item.getQuantity());
            return itemResponse;
        }).toList();

        response.setItems(itemResponses);
        return response;
    }
}
