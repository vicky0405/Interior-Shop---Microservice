package com.interiorshop.product_service.dto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductVariantListingResponse {

    private UUID id; // ID của Variant
    private UUID productId; // ID của Product cha (để link tới trang detail)
    private String name; // Tên Product Cha (VD: Sofa LANDSKRONA)
    private String sku;
    private BigDecimal price;
    private Map<String, Object> attributes; // Màu, size...
    private String imageUrl; // (Sau này có ảnh thì thêm vào)
}
