package com.interiorshop.product_service.repository;

import com.interiorshop.product_service.entity.Category;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    // Chỉ lấy những thằng Cha to nhất (parent là null)
    List<Category> findByParentIsNull();
}
