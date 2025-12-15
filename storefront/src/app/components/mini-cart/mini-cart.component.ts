import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartResponse } from '../../models/cart.model';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-mini-cart',
  imports: [CommonModule],
  templateUrl: './mini-cart.component.html',
  styleUrl: './mini-cart.component.css',
})
export class MiniCartComponent implements OnInit {
  cartData: CartResponse | null = null;

  constructor(private cartService: CartService) {}
  ngOnInit(): void {
    // Đăng ký "nghe đài". Bất cứ khi nào giỏ hàng thay đổi, biến cartData sẽ tự cập nhật
    this.cartService.cart$.subscribe((data) => {
      this.cartData = data;
    });
  }

  // Hàm xóa nhanh trong mini cart
  removeItem(variantId: string, event: Event) {
    event.stopPropagation(); // Chặn sự kiện click lan ra ngoài (để không bị đóng popup)
    this.cartService.removeFromCart(variantId).subscribe();
  }
}
