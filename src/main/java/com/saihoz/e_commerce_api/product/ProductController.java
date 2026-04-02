package com.saihoz.e_commerce_api.product;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable UUID id){
        try{
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product){
        productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID id,@Valid @RequestBody Product product){
        try {
            productService.getProductById(id);
            product.setId(id);
            productService.saveProduct(product);
            return ResponseEntity.ok("Product updated successfully");
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id){
        try{
            productService.getProductById(id);
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
