package com.saihoz.e_commerce_api.cart;

import com.saihoz.e_commerce_api.cart.dto.AddCartItemRequest;
import com.saihoz.e_commerce_api.cart.dto.CartResponse;
import com.saihoz.e_commerce_api.cart.dto.UpdateCartItemRequest;
import com.saihoz.e_commerce_api.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(cartService.getCart(user));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @RequestBody @Valid AddCartItemRequest request,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addItem(user, request));
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateItem(
            @PathVariable UUID cartItemId,
            @RequestBody @Valid UpdateCartItemRequest request,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(cartService.updateItem(user, cartItemId, request));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> removeItem(
            @PathVariable UUID cartItemId,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(cartService.removeItem(user, cartItemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @AuthenticationPrincipal User user) {

        cartService.clearCart(user);
        return ResponseEntity.noContent().build();
    }
}
