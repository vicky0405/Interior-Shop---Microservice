import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Quan trọng để dùng *ngFor
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { Product, ProductVariantItem } from '../../models/product.model';
import { Category } from '../../models/category.model';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css',
})
export class ProductListComponent implements OnInit {
  variants: ProductVariantItem[] = [];
  categories: Category[] = [];

  constructor(
    private productService: ProductService,
    private cartService: CartService,
  ) {}

  ngOnInit(): void {
    this.fetchProductVariants();
    this.fetchCategories();
  }

  fetchProductVariants() {
    this.productService.getAllVariants().subscribe({
      next: (data) => {
        this.variants = data;
        console.log('Dữ liệu lấy về:', data);
      },
      error: (err) => console.error('Lỗi:', err),
    });
  }

  fetchCategories() {
    this.productService.getCategories().subscribe((data) => (this.categories = data));
  }

  addToCart(event: Event, item: ProductVariantItem) {
    // 1. QUAN TRỌNG: Chặn click xuyên thấu
    // Nếu không có dòng này, khi bấm Add nó sẽ nhảy sang trang chi tiết luôn
    event.stopPropagation();
    event.preventDefault();

    // 2. Map dữ liệu từ Variant Item sang Cart Item
    // Lưu ý: item.id ở đây chính là variantId vì bạn đang list ra variants
    const cartItem = {
      productId: item.productId, // Cần đảm bảo model có field này
      variantId: item.id, // ID của biến thể
      productName: item.name,
      sku: item.sku + ' (' + (item.attributes?.color || '') + ')',
      attributes: item.attributes,
      price: item.price,
      quantity: 1,
      imageUrl: item.imageUrl,
    };

    // 3. Gọi Service thêm vào giỏ
    this.cartService.addToCart(cartItem).subscribe({
      next: (res) => {
        alert(`Đã thêm "${item.name}" vào giỏ hàng!`);
        // Lúc này Header sẽ tự nhảy số (nhờ BehaviorSubject ta đã làm)
      },
      error: (err) => {
        console.error('Lỗi thêm giỏ hàng:', err);
        alert('Có lỗi xảy ra, vui lòng thử lại.');
      },
    });
  }
}
