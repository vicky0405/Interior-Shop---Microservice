package com.interiorshop.product_service.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDetailResponse {

    private UUID id;
    private String name;
    private String description;
    private List<ProductVariantDetail> variants;

    // List ảnh chung của cha (để làm Gallery mặc định)
    private List<String> images;

    @Data
    @Builder
    public static class ProductVariantDetail {

        private UUID id;
        private String sku;
        private BigDecimal price;
        private Map<String, Object> attributes; // Màu, Size, Chất liệu (JSONB)
        // private String imageUrl; // Ảnh riêng của variant này (nếu có)
        private List<String> images;
        // Thông số kỹ thuật
        private Double width;
        private Double height;
        private Double depth;
    }
}
