package com.interiorshop.product_service.controller;

import com.interiorshop.product_service.dto.ProductDetailResponse;
import com.interiorshop.product_service.dto.ProductRequest;
import com.interiorshop.product_service.dto.ProductResponse;
import com.interiorshop.product_service.dto.ProductVariantListingResponse;
import com.interiorshop.product_service.entity.Product;
import com.interiorshop.product_service.service.ProductService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(
        @RequestBody ProductRequest request
    ) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/variants")
    public ResponseEntity<
        List<ProductVariantListingResponse>
    > getAllVariants() {
        return ResponseEntity.ok(productService.getAllVariants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProductById(
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
}
