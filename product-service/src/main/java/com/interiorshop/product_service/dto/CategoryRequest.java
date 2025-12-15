package com.interiorshop.product_service.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class CategoryRequest {

    private String name;
    private String slug; // Ví dụ: "phong-khach"
    private UUID parentId; // Quan trọng: Nếu là NULL thì là Cha, có ID thì là Con
}
