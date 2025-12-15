package com.interiorshop.product_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Khóa ngoại trỏ về Product cha
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore // Tránh vòng lặp vô tận khi convert sang JSON
    private Product product;

    @OneToMany(
        mappedBy = "variant",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<ProductMedia> medias;

    @Column(unique = true)
    private String sku;

    private BigDecimal price;

    // Lưu ý: Tồn kho (stock_qty) đã chuyển sang Inventory Service nên không có ở đây

    // Kích thước vật lý
    private Double weight;
    private Double width;
    private Double height;
    private Double depth;

    // --- JSONB MAGIC ---
    // Hibernate 6 tự động map Map<String, Object> thành JSONB trong Postgres
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "jsonb")
    private Map<String, Object> attributes;
    // Ví dụ data: {"color": "Red", "material": "Wood", "voltage": "220V"}
}
