package com.saihoz.e_commerce_api.cart.mapper;

import com.saihoz.e_commerce_api.cart.Cart;
import com.saihoz.e_commerce_api.cart.CartItem;
import com.saihoz.e_commerce_api.cart.dto.CartItemResponse;
import com.saihoz.e_commerce_api.cart.dto.CartResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {
    public CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream().map(item -> {
            CartItemResponse r = new CartItemResponse();
            r.setCartItemId(item.getId());
            r.setProductId(item.getProduct().getId());
            r.setProductName(item.getProduct().getName());
            r.setProductImage(item.getProduct().getImage());
            r.setUnitPrice(item.getProduct().getPrice());
            r.setQuantity(item.getQuantity());
            r.setSubtotal(item.getQuantity() * item.getProduct().getPrice());
            return r;
        }).toList();

        CartResponse response = new CartResponse();
        response.setCartId(cart.getId());
        response.setItems(itemResponses);
        response.setTotalItems(
                cart.getItems().stream()
                        .mapToInt(CartItem::getQuantity)
                        .sum()
        );
        response.setTotal(cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum());
        return response;
    }
}
