export interface Category {
  id: string;
  name: string;
  slug: string;
  children?: Category[]; // Dấu ? vì có thể không có con
}
