package com.interiorshop.product_service.controller;

import com.interiorshop.product_service.dto.CategoryRequest;
import com.interiorshop.product_service.dto.CategoryResponse;
import com.interiorshop.product_service.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
        @RequestBody CategoryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            categoryService.createCategory(request)
        );
    }
}
