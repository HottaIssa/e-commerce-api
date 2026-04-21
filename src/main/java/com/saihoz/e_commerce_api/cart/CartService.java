package com.saihoz.e_commerce_api.cart;

import com.saihoz.e_commerce_api.cart.dto.AddCartItemRequest;
import com.saihoz.e_commerce_api.cart.dto.CartResponse;
import com.saihoz.e_commerce_api.cart.dto.UpdateCartItemRequest;
import com.saihoz.e_commerce_api.cart.mapper.CartMapper;
import com.saihoz.e_commerce_api.exception.BadRequestException;
import com.saihoz.e_commerce_api.exception.ForbiddenException;
import com.saihoz.e_commerce_api.exception.ProductNotFoundException;
import com.saihoz.e_commerce_api.product.Product;
import com.saihoz.e_commerce_api.product.ProductRepository;
import com.saihoz.e_commerce_api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    public CartResponse getCart(User user) {
        Cart cart = getOrCreateCart(user);
        return cartMapper.mapToResponse(cart);
    }

    @Transactional
    public CartResponse addItem(User user, AddCartItemRequest request) {

        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado"));

        if (product.getStock() < request.getQuantity()) {
            throw new BadRequestException(
                    "Stock insuficiente. Disponible: " + product.getStock()
            );
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();

            if (newQuantity > product.getStock()) {
                throw new BadRequestException(
                        "No puedes agregar más de " + product.getStock() +
                                " unidades. Ya tienes " + item.getQuantity() + " en tu carrito."
                );
            }

            item.setQuantity(newQuantity);
            cartItemRepository.save(item);

        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cart.getItems().add(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.mapToResponse(savedCart);
    }

    @Transactional
    public CartResponse updateItem(User user, UUID cartItemId, UpdateCartItemRequest request) {

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ProductNotFoundException("Item no encontrado"));

        if (!item.getCart().getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("No tienes acceso a este item");
        }

        Product product = item.getProduct();
        if (request.getQuantity() > product.getStock()) {
            throw new BadRequestException(
                    "Stock insuficiente. Disponible: " + product.getStock()
            );
        }

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        Cart cart = item.getCart();
        return cartMapper.mapToResponse(cart);
    }

    @Transactional
    public CartResponse removeItem(User user, UUID cartItemId) {

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ProductNotFoundException("Item no encontrado"));

        if (!item.getCart().getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("No tienes acceso a este item");
        }

        Cart cart = item.getCart();
        cart.getItems().remove(item);
        cartRepository.save(cart);

        return cartMapper.mapToResponse(cart);
    }

    @Transactional
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

}