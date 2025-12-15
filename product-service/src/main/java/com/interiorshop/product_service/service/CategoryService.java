package com.interiorshop.product_service.service;

import com.interiorshop.product_service.dto.CategoryRequest;
import com.interiorshop.product_service.dto.CategoryResponse;
import com.interiorshop.product_service.entity.Category;
import com.interiorshop.product_service.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category parentCategory = null;

        // 1. Nếu có gửi parentId lên, tìm xem cha có tồn tại không
        if (request.getParentId() != null) {
            parentCategory = categoryRepository
                .findById(request.getParentId())
                .orElseThrow(() ->
                    new RuntimeException("Danh mục cha không tồn tại")
                );
        }

        // 2. Tạo Entity
        Category category = Category.builder()
            .name(request.getName())
            .slug(request.getSlug()) // Thực tế nên có hàm tự động convert Name -> Slug
            .parent(parentCategory) // Gán cha (hoặc null)
            .build();

        // 3. Lưu vào DB
        Category savedCategory = categoryRepository.save(category);

        // 4. Trả về DTO (Tái sử dụng hàm mapToCategoryResponse đã viết ở bước trước)
        return mapToCategoryResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        // Lấy tất cả category gốc (Root)
        List<Category> rootCategories = categoryRepository.findByParentIsNull();
        return rootCategories
            .stream()
            .map(this::mapToCategoryResponse)
            .collect(Collectors.toList());
    }

    // Hàm đệ quy: Map cha -> Map con -> Map cháu...
    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .slug(category.getSlug())
            .children(
                category
                    .getChildren()
                    .stream()
                    .map(this::mapToCategoryResponse) // Đệ quy gọi lại chính nó
                    .collect(Collectors.toList())
            )
            .build();
    }
}
