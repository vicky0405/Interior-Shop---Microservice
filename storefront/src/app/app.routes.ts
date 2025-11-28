import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'products',
    // Use lazy loading to load the standalone component
    loadComponent: () =>
      import('./features/products/feature/product-list.component').then(
        (m) => m.ProductListComponent,
      ),
  },
  // Redirect the root path to /products
  {
    path: '',
    redirectTo: 'products',
    pathMatch: 'full',
  },
];
