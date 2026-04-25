package com.saihoz.e_commerce_api.order;

import com.saihoz.e_commerce_api.cart.Cart;
import com.saihoz.e_commerce_api.cart.CartItem;
import com.saihoz.e_commerce_api.cart.CartRepository;
import com.saihoz.e_commerce_api.exception.BadRequestException;
import com.saihoz.e_commerce_api.exception.ForbiddenException;
import com.saihoz.e_commerce_api.exception.ResourceNotFoundException;
import com.saihoz.e_commerce_api.order.dto.CreateOrderRequestDTO;
import com.saihoz.e_commerce_api.order.dto.OrderResponseDTO;
import com.saihoz.e_commerce_api.order.mapper.OrderMapper;
import com.saihoz.e_commerce_api.product.Product;
import com.saihoz.e_commerce_api.product.ProductRepository;
import com.saihoz.e_commerce_api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDTO createOrder(User user, CreateOrderRequestDTO request) {

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No tienes un carrito activo"));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Tu carrito está vacío");
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException(
                        "Stock insuficiente para el producto: " + product.getName() +
                                ". Disponible: " + product.getStock()
                );
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(product.getPrice()); // Precio fijo al momento de comprar

            orderItems.add(orderItem);
        }

        // 4. Calcular total
        double total = orderItems.stream()
                .mapToDouble(orderItem -> orderItem.getUnitPrice() * orderItem.getQuantity())
                .sum();

        // 5. Crear la Order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(total);
        order.setShippingAddress(request.getShippingAddress());

        // 6. Asociar los items a la orden
        for (OrderItem item : orderItems) {
            item.setOrder(order);
        }
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        // 7. Vaciar el carrito
        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.mapToResponse(savedOrder);
    }

    // ─────────────────────────────────────────────
    // 2. VER MIS ÓRDENES
    //    El usuario ve su historial de compras
    // ─────────────────────────────────────────────
    public List<OrderResponseDTO> getMyOrders(User user) {
        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);
        return orders.stream()
                .map(orderMapper::mapToResponse)
                .toList();
    }

    // ─────────────────────────────────────────────
    // 3. VER DETALLE DE UNA ORDEN
    //    El usuario ve una orden específica suya
    // ─────────────────────────────────────────────
    public OrderResponseDTO getOrderById(UUID orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        // Seguridad: el usuario solo puede ver sus propias órdenes
        if (!order.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("No tienes acceso a esta orden");
        }

        return orderMapper.mapToResponse(order);
    }

    // ─────────────────────────────────────────────
    // 4. CAMBIAR ESTADO DE LA ORDEN (Admin / Webhook)
    //    Lo usa el admin para SHIPPED/DELIVERED
    //    y el webhook de pagos para cambiar a PAID
    // ─────────────────────────────────────────────
    @Transactional
    public OrderResponseDTO updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);
        return orderMapper.mapToResponse(orderRepository.save(order));
    }

    // ─────────────────────────────────────────────
    // 5. CANCELAR ORDEN
    //    Solo si está en PENDING (antes de pagar)
    // ─────────────────────────────────────────────
    @Transactional
    public OrderResponseDTO cancelOrder(UUID orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("No tienes acceso a esta orden");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Solo puedes cancelar órdenes en estado PENDING");
        }

        // Devolver el stock de cada producto
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderMapper.mapToResponse(orderRepository.save(order));
    }

    // ─────────────────────────────────────────────
    // 6. LISTAR TODAS LAS ÓRDENES (Admin)
    // ─────────────────────────────────────────────
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::mapToResponse);
    }

    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────

    // Valida que la transición de estado sea coherente
    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        Map<OrderStatus, List<OrderStatus>> allowed = Map.of(
                OrderStatus.PENDING,   List.of(OrderStatus.PAID, OrderStatus.CANCELLED),
                OrderStatus.PAID,      List.of(OrderStatus.SHIPPED),
                OrderStatus.SHIPPED,   List.of(OrderStatus.DELIVERED),
                OrderStatus.DELIVERED, List.of(),
                OrderStatus.CANCELLED, List.of()
        );

        if (!allowed.get(current).contains(next)) {
            throw new BadRequestException(
                    "No puedes cambiar el estado de " + current + " a " + next
            );
        }
    }
}
