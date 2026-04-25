package com.saihoz.e_commerce_api.product;

import com.saihoz.e_commerce_api.product.dto.ProductRequestDTO;
import com.saihoz.e_commerce_api.product.dto.ProductResponseDTO;
import com.saihoz.e_commerce_api.product.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@RequiredArgsConstructor
class ProductControllerTest {

    private final MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductMapper productMapper;

    private final ObjectMapper objectMapper;

    @Test
    void shouldCreateProduct() throws Exception {

        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Laptop");
        request.setPrice(1000.0);
        request.setStock(10);

        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(1000.0);
        product.setStock(10);

        Product savedProduct = new Product();
        savedProduct.setId(UUID.randomUUID());
        savedProduct.setName("Laptop");
        savedProduct.setPrice(1000.0);
        savedProduct.setStock(10);

        ProductResponseDTO response = new ProductResponseDTO();
        response.setName("Laptop");
        response.setPrice(1000.0);
        response.setStock(10);

        when(productMapper.toEntity(any(ProductRequestDTO.class))).thenReturn(product);
        when(productService.saveProduct(request)).thenReturn(response);
        when(productMapper.toDTO(savedProduct)).thenReturn(response);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void shouldReturnProductById() throws Exception {

        UUID id = UUID.randomUUID();

        Product product = new Product();
        product.setId(id);
        product.setName("Mouse");

        ProductResponseDTO response = new ProductResponseDTO();
        response.setId(id);
        response.setName("Mouse");

        when(productService.getProductById(id)).thenReturn(response);
        when(productMapper.toDTO(product)).thenReturn(response);

        mockMvc.perform(get("/api/v1/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mouse"));
    }

    @Test
    void shouldReturnAllProducts() throws Exception {

        Product product = new Product();
        product.setName("Keyboard");

        ProductResponseDTO response = new ProductResponseDTO();
        response.setName("Keyboard");

        when(productService.getAllProducts()).thenReturn(java.util.List.of(response));
        when(productMapper.toDTO(product)).thenReturn(response);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Keyboard"));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        UUID id = UUID.randomUUID();

        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Created Product");
        request.setPrice(200.00);
        request.setStock(20);

        Product createdProduct = new Product();
        createdProduct.setName("Created Product");
        createdProduct.setPrice(200.00);
        createdProduct.setStock(20);

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(100.00);
        updatedProduct.setStock(20);

        ProductResponseDTO response = new ProductResponseDTO();
        response.setName("Updated Product");
        response.setPrice(100.00);
        response.setStock(20);

        when(productMapper.toEntity(any(ProductRequestDTO.class)))
                .thenReturn(createdProduct);

        when(productService.updateProduct(id, request))
                .thenReturn(response);

        when(productMapper.toDTO(updatedProduct))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(productService).deleteProduct(id);

        mockMvc.perform(delete("/api/v1/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));
    }
}