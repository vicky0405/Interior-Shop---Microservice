import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ProductDetail, ProductVariantDetail } from '../../models/product.model';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { CartResponse } from '../../models/cart.model';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css',
})
export class ProductDetailComponent implements OnInit {
  product: ProductDetail | null = null;
  selectedVariant: ProductVariantDetail | null = null;
  currentMainImage: string = '';
  currentGallery: string[] = [];
  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.productService.getProductById(id).subscribe((data) => {
        this.product = data;
        if (data.variants && data.variants.length) {
          this.selectVariant(data.variants[0]);
        }
        // Mặc định hiển thị ảnh đầu tiên của cha (nếu variant ko có ảnh)
        if (data.variants && data.variants.length > 0) {
          this.selectVariant(data.variants[0]);
        } else {
          // Trường hợp sản phẩm không có variant (Simple), chỉ hiện ảnh cha
          this.currentGallery = data.images;
          this.currentMainImage = data.images[0];
        }
      });
    }
  }
  selectVariant(variant: ProductVariantDetail) {
    this.selectedVariant = variant;

    // LOGIC GỘP ẢNH: Ảnh Cha + Ảnh của Variant này
    let newGallery = [...(this.product?.images || [])]; // Lấy ảnh cha trước

    if (variant.images && variant.images.length > 0) {
      newGallery = [...newGallery, ...variant.images]; // Cộng thêm ảnh con
    }

    // Cập nhật Gallery hiển thị
    this.currentGallery = newGallery;

    // Tự động nhảy ảnh to về ảnh đầu tiên của variant (nếu có), hoặc ảnh đầu gallery
    if (variant.images && variant.images.length > 0) {
      this.currentMainImage = variant.images[0];
    } else if (newGallery.length > 0) {
      this.currentMainImage = newGallery[0];
    }
  }
  // Hàm helper để check xem variant nào đang active (để tô viền)
  isSelected(variant: ProductVariantDetail): boolean {
    return this.selectedVariant?.id === variant.id;
  }
  addToCart() {
    // 1. Kiểm tra xem đã chọn biến thể chưa
    if (!this.selectedVariant || !this.product) {
      alert('Vui lòng chọn màu sắc/kích thước!');
      return;
    }

    // 2. Tạo object CartItem đúng format Backend yêu cầu
    const cartItem = {
      productId: this.product.id,
      variantId: this.selectedVariant.id,
      productName: this.product.name,
      // Lấy tên màu để hiển thị trong giỏ cho dễ nhìn (nếu có)
      sku: this.selectedVariant.sku + ' (' + (this.selectedVariant.attributes?.color || '') + ')',
      price: this.selectedVariant.price,
      quantity: 1, // Tạm thời để hardcode là 1
      imageUrl: this.currentMainImage, // Lấy luôn cái ảnh đang hiện to đùng
    };

    // 3. Gọi Service
    this.cartService.addToCart(cartItem).subscribe({
      next: (res: CartResponse) => {
        alert(res.message);
        console.log('Tổng số lượng mới:', res.totalItems);
        console.log('Tổng tiền mới:', res.totalPrice);
      },
      error: (err) => {
        console.error('Lỗi thêm giỏ hàng:', err);
        alert('Có lỗi xảy ra, vui lòng thử lại.');
      },
    });
  }
}
