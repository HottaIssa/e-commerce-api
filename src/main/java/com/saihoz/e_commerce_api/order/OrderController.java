package com.saihoz.e_commerce_api.order;

import com.saihoz.e_commerce_api.order.dto.CreateOrderRequestDTO;
import com.saihoz.e_commerce_api.order.dto.OrderResponseDTO;
import com.saihoz.e_commerce_api.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ─────────────────────────────────────────────
    // RUTAS DEL USUARIO AUTENTICADO
    // ─────────────────────────────────────────────

    // Momento: usuario hace clic en "Confirmar compra"
    @PostMapping("/v1/orders")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @RequestBody @Valid CreateOrderRequestDTO request,
            @AuthenticationPrincipal User user) {

        OrderResponseDTO order = orderService.createOrder(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    // Momento: usuario entra a "Mis pedidos"
    @GetMapping("/v1/orders")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(orderService.getMyOrders(user));
    }

    // Momento: usuario hace clic en una orden específica para ver el detalle
    @GetMapping("/v1/orders/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(orderService.getOrderById(id, user));
    }

    // Momento: usuario cancela una orden antes de pagar
    @PatchMapping("/v1/orders/{id}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(orderService.cancelOrder(id, user));
    }

    // ─────────────────────────────────────────────
    // RUTAS DEL ADMINISTRADOR
    // ─────────────────────────────────────────────

    // Momento: admin entra al panel de gestión de pedidos
    @GetMapping("/v1/admin/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    // Momento: admin cambia el estado a SHIPPED o DELIVERED
    @PatchMapping("/v1/admin/orders/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status) {

        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

}
