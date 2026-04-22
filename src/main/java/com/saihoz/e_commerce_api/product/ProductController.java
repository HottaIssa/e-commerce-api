package com.saihoz.e_commerce_api.product;

import com.saihoz.e_commerce_api.product.dto.ProductRequestDTO;
import com.saihoz.e_commerce_api.product.dto.ProductResponseDTO;
import com.saihoz.e_commerce_api.product.mapper.ProductMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    private final ProductMapper productMapper;

    private final CategoryRepository categoryRepository;

    public ProductController(ProductService productService, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {

        List<Product> products = productService.getAllProducts();

        return ResponseEntity.ok(
                productMapper.toDTOList(products)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                productMapper.toDTO(productService.getProductById(id))
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO request) {

        Product product = productMapper.toEntity(request);

        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());

        product.setCategories(categories);


        Product saved = productService.saveProduct(product);

        ProductResponseDTO response = productMapper.toDTO(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductRequestDTO request) {

        Product product = productMapper.toEntity(request);

        Product updated = productService.updateProduct(id, product);

        ProductResponseDTO response = productMapper.toDTO(updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

}
