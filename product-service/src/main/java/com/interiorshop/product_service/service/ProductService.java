package com.interiorshop.product_service.service;

import com.interiorshop.product_service.model.Product;
import com.interiorshop.product_service.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAllProducts() {
        // Business Logic: Check and populate dummy data
        if (productRepository.count() == 0) {
            Product p1 = new Product();
            p1.setName("Angular T-Shirt");
            p1.setDescription("A shirt for Angular developers.");
            p1.setPrice(25.99);
            productRepository.save(p1);

            Product p2 = new Product();
            p2.setName("Spring Boot Mug");
            p2.setDescription("Mug for Java enthusiasts.");
            p2.setPrice(15.50);
            productRepository.save(p2);
        }

        // Actual data fetching logic
        return productRepository.findAll();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
