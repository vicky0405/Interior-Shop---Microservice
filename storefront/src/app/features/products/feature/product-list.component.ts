import { Component, OnInit } from '@angular/core';
import { NgFor, NgIf, CurrencyPipe } from '@angular/common';
import { Product } from '../product.model';
import { ProductService } from '../data-access/product.service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [NgFor, NgIf, CurrencyPipe],
  template: `
    <h2>Product Catalog</h2>
    <div *ngIf="products.length > 0; else loading">
      <div
        *ngFor="let product of products"
        class="product-card"
        style="border: 1px solid #eee; margin-bottom: 10px; padding: 10px;"
      >
        <h3>{{ product.name }}</h3>
        <p>{{ product.description }}</p>
        <p><strong>Price:</strong> {{ product.price | currency: 'USD' }}</p>
      </div>
    </div>
    <ng-template #loading><p>Loading products...</p></ng-template>
  `,
  styles: [
    `
      .product-card {
        max-width: 400px;
      }
    `,
  ],
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.products = data;
        console.log('PRODUCT ' + this.products);
      },
      error: (err) => {
        console.error('API Error:', err);
      },
    });
  }
}
