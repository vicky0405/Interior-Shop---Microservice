import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { MiniCartComponent } from '../mini-cart/mini-cart.component';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink, MiniCartComponent], // Nhớ import MiniCartComponent
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent implements OnInit {
  cartCount = 0;

  constructor(private cartService: CartService) {}

  ngOnInit() {
    // Đăng ký nhận tin từ "Đài phát thanh" CartService
    // Để khi user thêm hàng ở bất cứ đâu, số này cũng tự nhảy
    this.cartService.cart$.subscribe((data) => {
      this.cartCount = data ? data.totalItems : 0;
    });
  }
}
