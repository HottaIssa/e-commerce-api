package com.saihoz.e_commerce_api.product;

import com.saihoz.e_commerce_api.config.StorageService;
import com.saihoz.e_commerce_api.exception.ProductNotFoundException;
import com.saihoz.e_commerce_api.product.dto.ProductRequestDTO;
import com.saihoz.e_commerce_api.product.dto.ProductResponseDTO;
import com.saihoz.e_commerce_api.product.mapper.ProductMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldReturnAllProducts() {
        List<Product> products = List.of(new Product(), new Product());
        List<ProductResponseDTO> response = List.of(new ProductResponseDTO(), new ProductResponseDTO());

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDTOList(products)).thenReturn(response);

        List<ProductResponseDTO> result = productService.getAllProducts();

        assertEquals(2, result.size());
        verify(productRepository).findAll();
        verify(productMapper).toDTOList(products);
    }

    @Test
    void shouldReturnProductById() {
        UUID id = UUID.randomUUID();

        Product product = new Product();
        ProductResponseDTO response = new ProductResponseDTO();

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(response);

        ProductResponseDTO result = productService.getProductById(id);

        assertNotNull(result);
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        UUID id = UUID.randomUUID();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(id);
        });
    }

    @Test
    void shouldSaveProductWithImage() {

        ProductRequestDTO request = new ProductRequestDTO();
        request.setCategoryIds(List.of(1L, 2L));
        request.setImage(mock(MultipartFile.class));

        Product product = new Product();
        product.setId(null); // aún no persistido

        Product savedProduct = new Product();
        savedProduct.setId(UUID.randomUUID());

        List<Category> categories = List.of(new Category());
        ProductResponseDTO response = new ProductResponseDTO();

        when(productMapper.toEntity(request)).thenReturn(product);
        when(categoryRepository.findAllById(request.getCategoryIds())).thenReturn(categories);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(storageService.uploadImage(savedProduct.getId(), request.getImage()))
                .thenReturn("image-key");
        when(productMapper.toDTO(savedProduct)).thenReturn(response);

        ProductResponseDTO result = productService.saveProduct(request);

        assertNotNull(result);

        verify(productRepository).save(product);
        verify(storageService).uploadImage(savedProduct.getId(), request.getImage());
    }

    @Test
    void shouldUpdateProductWithoutImage() {

        UUID id = UUID.randomUUID();

        ProductRequestDTO request = new ProductRequestDTO();

        Product existing = new Product();
        Product mapped = new Product();
        Product saved = new Product();
        ProductResponseDTO response = new ProductResponseDTO();

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productMapper.toEntity(request)).thenReturn(mapped);
        when(productRepository.save(existing)).thenReturn(saved);
        when(productMapper.toDTO(saved)).thenReturn(response);

        ProductResponseDTO result = productService.updateProduct(id, request);

        assertNotNull(result);

        verify(productRepository).save(existing);

        // 🔥 importante: NO se llama storage
        verify(storageService, never()).uploadImage(any(), any());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingProduct() {
        UUID id = UUID.randomUUID();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(id, new ProductRequestDTO());
        });
    }

    @Test
    void shouldUpdateProductImage() {

        UUID id = UUID.randomUUID();
        MultipartFile file = mock(MultipartFile.class);

        Product existing = new Product();
        existing.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(storageService.uploadImage(id, file)).thenReturn("new-image");

        productService.updateImageProduct(id, file);

        assertEquals("new-image", existing.getImage());

        verify(storageService).uploadImage(id, file);
    }

    @Test
    void shouldThrowWhenUpdatingImageNonExistingProduct() {
        UUID id = UUID.randomUUID();
        MultipartFile file = mock(MultipartFile.class);

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateImageProduct(id, file);
        });
    }

    @Test
    void shouldDeleteProduct() {

        UUID id = UUID.randomUUID();

        when(productRepository.findById(id)).thenReturn(Optional.of(new Product()));

        productService.deleteProduct(id);

        verify(productRepository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingProduct() {

        UUID id = UUID.randomUUID();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(id);
        });
    }
}