package com.saihoz.e_commerce_api.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(UUID id){
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void saveProduct(Product product){
        productRepository.save(product);
    }

    public void deleteProduct(UUID id){
        productRepository.deleteById(id);
    }

}
