package com.interiorshop.product_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "product_medias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Link về Product Cha
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    // Link về Variant (QUAN TRỌNG: Cột này có thể Null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = true)
    @JsonIgnore
    private ProductVariant variant;

    @Column(nullable = false)
    private String url;

    @Column(name = "media_type")
    private String mediaType; // 'image', 'video'

    @Column(name = "is_primary")
    private Boolean isPrimary; // true nếu là ảnh đại diện
}
