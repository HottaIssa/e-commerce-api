package com.saihoz.e_commerce_api.product;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable UUID id){
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category){
        categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable UUID id,@Valid @RequestBody Category category){
        try {
            categoryService.getCategoryById(id);
            category.setId(id);
            categoryService.saveCategory(category);
            return ResponseEntity.ok("Category updated successfully");
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID id){
        try {
            categoryService.getCategoryById(id);
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
