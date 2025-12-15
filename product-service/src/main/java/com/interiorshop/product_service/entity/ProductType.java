package com.interiorshop.product_service.entity;

public enum ProductType {
    SIMPLE, // Sản phẩm đơn giản (không có biến thể, bán lẻ)
    VARIANT, // Sản phẩm cha của các biến thể (Ví dụ: Áo thun)
    BUNDLE, // Combo (Gồm nhiều sản phẩm khác)
    PART, // Linh kiện (Ốc vít, chân bàn)
}
