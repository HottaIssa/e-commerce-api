package com.saihoz.e_commerce_api.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(UUID id){
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void saveCategory(Category category){
        categoryRepository.save(category);
    }

    public void deleteCategory(UUID id){
        categoryRepository.deleteById(id);
    }
}
