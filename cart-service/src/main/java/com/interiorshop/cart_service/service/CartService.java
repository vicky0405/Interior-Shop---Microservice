package com.interiorshop.cart_service.service;

import com.interiorshop.cart_service.dto.CartResponse;
import com.interiorshop.cart_service.model.CartItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    // Lưu ý: Nên để <String, Object> để tránh lỗi cast khi dùng opsForHash
    private final RedisTemplate<String, CartItem> redisTemplate;

    private static final String CART_PREFIX = "cart:";
    private static final long CART_TTL_DAYS = 30;

    // =========================================================================
    // PHẦN 1: CÁC HÀM LOGIC CHÍNH (Trả về CartResponse)
    // =========================================================================

    // 1. Lấy thông tin giỏ hàng (Bao gồm cả tính tổng)
    public CartResponse getCart(String sessionId) {
        return fetchCartResponse(sessionId, "Get cart success");
    }

    // 2. Thêm vào giỏ -> Trả về giỏ hàng mới nhất
    public CartResponse addToCart(String sessionId, CartItem item) {
        String key = CART_PREFIX + sessionId;

        // Logic check tồn tại
        Object existingItem = redisTemplate
            .opsForHash()
            .get(key, item.getVariantId());

        if (existingItem != null) {
            CartItem current = (CartItem) existingItem;
            current.setQuantity(current.getQuantity() + item.getQuantity());
            // Cập nhật lại giá nếu cần (tuỳ nghiệp vụ, ở đây mình giữ giá cũ)
            redisTemplate.opsForHash().put(key, item.getVariantId(), current);
        } else {
            redisTemplate.opsForHash().put(key, item.getVariantId(), item);
        }

        // Reset TTL
        redisTemplate.expire(key, CART_TTL_DAYS, TimeUnit.DAYS);

        // Trả về giỏ hàng đã tính toán lại
        return fetchCartResponse(sessionId, "Item added successfully");
    }

    // 3. Xóa 1 món -> Trả về giỏ hàng mới nhất
    public CartResponse removeFromCart(String sessionId, String variantId) {
        String key = CART_PREFIX + sessionId;
        redisTemplate.opsForHash().delete(key, variantId);

        return fetchCartResponse(sessionId, "Item removed successfully");
    }

    // 4. Xóa sạch -> Trả về giỏ rỗng
    public CartResponse clearCart(String sessionId) {
        String key = CART_PREFIX + sessionId;
        redisTemplate.delete(key);

        // Trả về object rỗng
        return CartResponse.builder()
            .message("Cart cleared successfully")
            .items(new ArrayList<>())
            .totalItems(0)
            .totalPrice(BigDecimal.ZERO)
            .build();
    }

    // =========================================================================
    // PHẦN 2: HELPER METHODS (Hàm phụ trợ)
    // =========================================================================

    // Helper: Lấy List<CartItem> từ Redis (Code cũ của bạn chuyển vào đây)
    private List<CartItem> getCartItemsFromRedis(String sessionId) {
        String key = CART_PREFIX + sessionId;
        return redisTemplate
            .opsForHash()
            .values(key)
            .stream()
            .map(obj -> (CartItem) obj)
            .collect(Collectors.toList());
    }

    // Helper: Tính toán tổng tiền và đóng gói vào CartResponse
    private CartResponse fetchCartResponse(String sessionId, String message) {
        List<CartItem> items = getCartItemsFromRedis(sessionId);

        int totalItems = items.stream().mapToInt(CartItem::getQuantity).sum();
        BigDecimal totalPrice = items
            .stream()
            .map(CartItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
            .message(message)
            .items(items)
            .totalItems(totalItems)
            .totalPrice(totalPrice)
            .build();
    }
}
