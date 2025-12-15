package com.interiorshop.product_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Lưu Category ID dưới dạng UUID đơn giản
    // (Ta không map @ManyToOne sang bảng Category ở đây để tránh phức tạp ban đầu)
    @Column(name = "category_id")
    private UUID categoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false)
    private ProductType productType;

    @Column(name = "is_saleable")
    private boolean isSaleable = true;

    // Mối quan hệ 1-N với Variants
    // mappedBy trỏ tới field "product" bên class ProductVariant
    @OneToMany(
        mappedBy = "product",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<ProductVariant> variants;

    @OneToMany(
        mappedBy = "product",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<ProductMedia> medias;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
