package com.interiorshop.product_service.dto;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

    private UUID id;
    private String name;
    private String slug;
    private List<CategoryResponse> children; // List con lồng bên trong
}
