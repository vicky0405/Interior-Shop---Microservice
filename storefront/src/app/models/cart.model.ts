// src/app/models/cart.model.ts

export interface CartResponse {
  message: string;
  totalItems: number;
  totalPrice: number;
  items: any[]; // Hoặc CartItem[] nếu bạn đã define
}
