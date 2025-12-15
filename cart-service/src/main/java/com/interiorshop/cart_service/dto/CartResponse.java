package com.interiorshop.cart_service.dto;

import com.interiorshop.cart_service.model.CartItem;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {

    private String message;

    // Thêm thông tin này để update UI Header
    private int totalItems;
    private BigDecimal totalPrice;

    // (Tuỳ chọn) Trả về luôn danh sách items nếu muốn update cả mini-cart
    private List<CartItem> items;
}
