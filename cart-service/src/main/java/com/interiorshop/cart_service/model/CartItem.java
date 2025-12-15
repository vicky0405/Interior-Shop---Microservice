package com.interiorshop.cart_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    private String productId; // ID sản phẩm cha
    private String variantId; // ID biến thể (SKU thực tế)
    private String productName; // Snapshot tên lúc mua
    private String sku; // Snapshot SKU
    private String imageUrl; // Ảnh thumbnail
    private BigDecimal price; // Giá lúc mua
    private int quantity; // Số lượng

    // Tính tổng tiền của item này (Price * Qty)
    @JsonIgnore
    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
