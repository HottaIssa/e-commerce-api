package com.saihoz.e_commerce_api.product;

import com.saihoz.e_commerce_api.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldReturnAllProducts() {
        List<Product> products = List.of(new Product(), new Product());

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        verify(productRepository).findAll();
    }

    @Test
    void shouldReturnProductWhenIdExists() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(id);

        assertEquals(id, result.getId());
        verify(productRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        UUID id = UUID.randomUUID();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.getProductById(id);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void shouldSaveProduct() {
        Product product = new Product();
        product.setName("Laptop");

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.saveProduct(product);

        assertEquals("Laptop", result.getName());
        verify(productRepository).save(product);
    }

    @Test
    void shouldUpdateProduct() {
        UUID id = UUID.randomUUID();

        Product existing = new Product();
        existing.setId(id);
        existing.setName("Old");

        Product updated = new Product();
        updated.setName("New");

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenReturn(updated);

        Product result = productService.updateProduct(id, updated);

        assertEquals("New", result.getName());
        verify(productRepository).findById(id);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingProduct() {
        UUID id = UUID.randomUUID();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(id, new Product());
        });
    }

    @Test
    void shouldDeleteProduct() {
        UUID id = UUID.randomUUID();

        when(productRepository.findById(id)).thenReturn(Optional.of(new Product()));
        doNothing().when(productRepository).deleteById(id);

        productService.deleteProduct(id);

        verify(productRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingProduct() {
        UUID id = UUID.randomUUID();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(id);
        });
    }
}