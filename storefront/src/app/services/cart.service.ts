import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { v4 as uuidv4 } from 'uuid'; // Import hàm sinh ID
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { CartResponse } from '../models/cart.model';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private apiUrl = 'http://localhost:8082/api/cart'; // Port 8082 của Cart Service

  // 1. TẠO "ĐÀI PHÁT THANH" (BehaviorSubject)
  // Mặc định ban đầu là null hoặc object rỗng
  private cartSubject = new BehaviorSubject<CartResponse | null>(null);

  // 2. Public ra một biến Observable để các Component khác "lắng nghe"
  cart$ = this.cartSubject.asObservable();
  constructor(private http: HttpClient) {
    this.getCart().subscribe();
  }

  // --- API: LẤY GIỎ HÀNG ---
  getCart(): Observable<CartResponse> {
    return this.http.get<CartResponse>(this.apiUrl, { headers: this.getHeaders() }).pipe(
      tap((response) => {
        this.cartSubject.next(response); // Phát tin mới nhất
      }),
    );
  }

  // --- API: THÊM VÀO GIỎ ---
  addToCart(item: any): Observable<CartResponse> {
    const headers = this.getHeaders();
    return this.http.post<CartResponse>(`${this.apiUrl}/add`, item, { headers: headers }).pipe(
      tap((response) => {
        this.cartSubject.next(response);
      }),
    );
  }

  // --- API: XÓA MÓN ---
  removeFromCart(variantId: string): Observable<CartResponse> {
    return this.http
      .delete<CartResponse>(`${this.apiUrl}/${variantId}`, { headers: this.getHeaders() })
      .pipe(
        tap((response) => {
          this.cartSubject.next(response); // Phát tin mới nhất (số lượng đã giảm)
        }),
      );
  }

  // --- LOGIC 1: LẤY HOẶC TẠO GUEST ID ---
  private getGuestId(): string {
    // 1. Kiểm tra xem trong LocalStorage đã có chưa
    let guestId = localStorage.getItem('guest_id');

    // 2. Nếu chưa có (khách mới tinh) -> Tạo mới và lưu lại
    if (!guestId) {
      guestId = uuidv4();
      localStorage.setItem('guest_id', guestId!);
    }

    return guestId!;
  }

  // --- LOGIC 2: TẠO HEADER CHỨA ID ---
  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'X-Guest-Id': this.getGuestId(), // Gửi kèm ID trong mọi request
    });
  }
}
