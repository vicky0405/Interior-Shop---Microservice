package com.interiorshop.cart_service.controller;

import com.interiorshop.cart_service.dto.CartResponse;
import com.interiorshop.cart_service.model.CartItem;
import com.interiorshop.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 1. Lấy giỏ hàng (GET)
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
        @RequestHeader("X-Guest-Id") String guestId
    ) {
        return ResponseEntity.ok(cartService.getCart(getSessionId(guestId)));
    }

    // 2. Thêm vào giỏ (POST)
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(
        @RequestHeader("X-Guest-Id") String guestId,
        @RequestBody CartItem item
    ) {
        CartResponse response = cartService.addToCart(
            getSessionId(guestId),
            item
        );

        // Trả về HTTP Status 201 ở Header + JSON Body
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // Header là 201 // Body chứa { "status": 201 ... }
    }

    // 3. Xóa 1 món (DELETE)
    @DeleteMapping("/{variantId}")
    public ResponseEntity<CartResponse> removeFromCart(
        @RequestHeader("X-Guest-Id") String guestId,
        @PathVariable String variantId
    ) {
        CartResponse response = cartService.removeFromCart(
            getSessionId(guestId),
            variantId
        );
        return ResponseEntity.ok(response);
    }

    // 4. Xóa sạch giỏ (DELETE)
    @DeleteMapping("/clear")
    public ResponseEntity<CartResponse> clearCart(
        @RequestHeader("X-Guest-Id") String guestId
    ) {
        CartResponse response = cartService.clearCart(getSessionId(guestId));
        return ResponseEntity.ok(response);
    }

    // Helper: Validate ID khách hàng
    private String getSessionId(String guestId) {
        if (guestId == null || guestId.trim().isEmpty()) {
            throw new IllegalArgumentException("X-Guest-Id header is required");
        }
        return guestId;
    }
}
