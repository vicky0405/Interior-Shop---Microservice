import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product, ProductDetail, ProductVariantItem } from '../models/product.model';
import { Category } from '../models/category.model';
@Injectable({
  providedIn: 'root',
})
export class ProductService {
  // Trỏ thẳng vào Product Service backend
  private apiUrl = 'http://localhost:8080/api/product';

  constructor(private http: HttpClient) {}

  createProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product);
  }

  // Giả sử bạn sẽ viết thêm API lấy danh sách sau này
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  getAllVariants(): Observable<ProductVariantItem[]> {
    return this.http.get<ProductVariantItem[]>(this.apiUrl + '/variants');
  }
  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>('http://localhost:8080/api/category');
  }
  getProductById(id: string): Observable<ProductDetail> {
    return this.http.get<ProductDetail>(this.apiUrl + `/${id}`);
  }
}
