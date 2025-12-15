package com.interiorshop.product_service.repository;

import com.interiorshop.product_service.entity.ProductVariant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository
    extends JpaRepository<ProductVariant, UUID> {}
