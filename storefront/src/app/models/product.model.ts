export interface Product {
  id?: string; // Dấu ? nghĩa là có thể null (khi tạo mới chưa có ID)
  name: string;
  description: string;
  productType: 'SIMPLE' | 'VARIANT' | 'BUNDLE' | 'PART';
}

export interface ProductVariantItem {
  id: string; // ID variant
  productId: string; // ID cha
  name: string; // Tên cha
  sku: string;
  price: number;
  attributes: any;
  imageUrl: string;
}

export interface ProductDetail {
  id: string;
  name: string;
  description: string;
  images: string[];
  variants: ProductVariantDetail[];
}

export interface ProductVariantDetail {
  id: string;
  sku: string;
  price: number;
  attributes: any;
  images: string[];
  width?: number;
  height?: number;
  depth?: number;
}
