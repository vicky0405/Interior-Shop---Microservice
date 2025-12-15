package com.interiorshop.product_service.dto;

import com.interiorshop.product_service.entity.ProductType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Data;

@Data
public class ProductRequest {

    private String name;
    private String description;
    private UUID categoryId;
    private ProductType productType; // SIMPLE, VARIANT, BUNDLE, PART
    private List<VariantRequest> variants;

    @Data
    public static class VariantRequest {

        private String sku;
        private BigDecimal price;
        private Double weight;
        private Double width;
        private Double height;
        private Double depth;

        // Đây là chỗ hứng cục JSONB (Màu, Size, Chất liệu...)
        private Map<String, Object> attributes;
    }
}
