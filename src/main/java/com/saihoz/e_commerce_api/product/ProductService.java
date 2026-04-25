package com.saihoz.e_commerce_api.product;

import com.saihoz.e_commerce_api.config.StorageService;
import com.saihoz.e_commerce_api.exception.ProductNotFoundException;
import com.saihoz.e_commerce_api.product.dto.ProductRequestDTO;
import com.saihoz.e_commerce_api.product.dto.ProductResponseDTO;
import com.saihoz.e_commerce_api.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final StorageService storageService;

    public List<ProductResponseDTO> getAllProducts() {
        return productMapper.toDTOList(productRepository.findAll());
    }

    public ProductResponseDTO getProductById(UUID id) {
        return productMapper.toDTO(productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found")));
    }

    @Transactional
    public ProductResponseDTO saveProduct(ProductRequestDTO request) {
        Product product = productMapper.toEntity(request);

        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
        product.setCategories(categories);

        Product savedProduct = productRepository.save(product);

        String imageKey = storageService.uploadImage(savedProduct.getId(), request.getImage());

        savedProduct.setImage(imageKey);


        return productMapper.toDTO(savedProduct);
    }

    @Transactional
    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO request) {

        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));

        Product product = productMapper.toEntity(request);

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setCategories(product.getCategories());

        return productMapper.toDTO(productRepository.save(existingProduct));
    }

    @Transactional
    public void updateImageProduct(UUID id, MultipartFile file){
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));

        String imageKey = storageService.uploadImage(existingProduct.getId(), file);

        existingProduct.setImage(imageKey);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        productRepository.deleteById(id);
    }

}
