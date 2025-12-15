package com.interiorshop.product_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String slug; // Ví dụ: "phong-khach" (để làm URL đẹp)

    // ĐỆ QUY: Category con trỏ về cha
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore // Ngắt vòng lặp khi serialize JSON
    private Category parent;

    // ĐỆ QUY: Cha chứa danh sách con
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @Builder.Default // <--- THÊM DÒNG NÀY
    private List<Category> children = new ArrayList<>();
}
