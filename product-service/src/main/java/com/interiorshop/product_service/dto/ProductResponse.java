package com.interiorshop.product_service.dto;

import com.interiorshop.product_service.entity.ProductType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    private UUID id;
    private String name;
    private String description;
    private ProductType productType;
    private List<ProductVariantResponse> variants;

    @Data
    @Builder
    public static class ProductVariantResponse {

        private UUID id;
        private String sku;
        private BigDecimal price;
        private Map<String, Object> attributes; // JSONB
        private Double width;
        private Double height;
        private Double depth;
        private Double weight;
    }
}
