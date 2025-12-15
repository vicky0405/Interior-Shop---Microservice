import { Routes } from '@angular/router';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';

export const routes: Routes = [
  { path: '', component: ProductListComponent }, // Trang chủ là danh sách
  { path: 'product/:id', component: ProductDetailComponent },
];
