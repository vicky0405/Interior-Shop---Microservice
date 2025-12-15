package com.interiorshop.product_service.service;

import com.interiorshop.product_service.dto.ProductDetailResponse;
import com.interiorshop.product_service.dto.ProductRequest;
import com.interiorshop.product_service.dto.ProductResponse;
import com.interiorshop.product_service.dto.ProductVariantListingResponse;
import com.interiorshop.product_service.entity.Product;
import com.interiorshop.product_service.entity.ProductMedia;
import com.interiorshop.product_service.entity.ProductVariant;
import com.interiorshop.product_service.repository.ProductRepository;
import com.interiorshop.product_service.repository.ProductVariantRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // Tự động inject Repository (khỏi cần @Autowired)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;

    @Transactional // Đảm bảo lưu Cha thành công mới lưu Con
    public Product createProduct(ProductRequest request) {
        // 1. Tạo Product (Cha)
        Product product = Product.builder()
            .name(request.getName())
            .description(request.getDescription())
            .categoryId(request.getCategoryId())
            .productType(request.getProductType())
            .isSaleable(true)
            .build();

        // 2. Map danh sách Variants (Con)
        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            List<ProductVariant> variants = request
                .getVariants()
                .stream()
                .map(vRequest ->
                    ProductVariant.builder()
                        .product(product) // Gắn cha vào con
                        .sku(vRequest.getSku())
                        .price(vRequest.getPrice())
                        .weight(vRequest.getWeight())
                        .width(vRequest.getWidth())
                        .height(vRequest.getHeight())
                        .depth(vRequest.getDepth())
                        .attributes(vRequest.getAttributes()) // JSONB magic
                        .build()
                )
                .collect(Collectors.toList());

            product.setVariants(variants);
        }

        // 3. Lưu tất cả vào DB (Cascade sẽ tự lưu Variants)
        return productRepository.save(product);
    }

    @Transactional(readOnly = true) // readOnly giúp tối ưu hiệu năng cho thao tác đọc
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        // Map từ Entity -> DTO Response
        return products
            .stream()
            .map(this::mapToProductResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductVariantListingResponse> getAllVariants() {
        // Lấy tất cả biến thể từ DB
        List<ProductVariant> variants = productVariantRepository.findAll();

        return variants
            .stream()
            .map(variant -> mapToVariantListing(variant))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductById(UUID productId) {
        Product product = productRepository
            .findById(productId)
            .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // 1. Lấy ảnh chung của Cha
        List<String> productImages = new ArrayList<>();
        if (product.getMedias() != null) {
            productImages = product
                .getMedias()
                .stream()
                .filter(media -> media.getVariant() == null)
                .map(ProductMedia::getUrl)
                .collect(Collectors.toList());
        }

        // 2. Map danh sách biến thể
        List<ProductDetailResponse.ProductVariantDetail> variantDetails =
            product
                .getVariants()
                .stream()
                .map(v -> {
                    // SỬA: Lấy toàn bộ danh sách ảnh của variant này
                    List<String> variantImages = new ArrayList<>();
                    if (v.getMedias() != null && !v.getMedias().isEmpty()) {
                        variantImages = v
                            .getMedias()
                            .stream()
                            .map(ProductMedia::getUrl)
                            .collect(Collectors.toList());
                    }

                    return ProductDetailResponse.ProductVariantDetail.builder()
                        .id(v.getId())
                        .sku(v.getSku())
                        .price(v.getPrice())
                        .attributes(v.getAttributes())
                        .images(variantImages) // Có thể null nếu không có ảnh riêng
                        .width(v.getWidth())
                        .height(v.getHeight())
                        .depth(v.getDepth())
                        .build();
                })
                .collect(Collectors.toList());

        return ProductDetailResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .images(productImages)
            .variants(variantDetails)
            .build();
    }

    // Hàm phụ trợ để map dữ liệu (cho code đỡ rối)
    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .productType(product.getProductType())
            .variants(
                product
                    .getVariants()
                    .stream()
                    .map(this::mapToVariantResponse)
                    .collect(Collectors.toList())
            )
            .build();
    }

    private ProductResponse.ProductVariantResponse mapToVariantResponse(
        ProductVariant variant
    ) {
        return ProductResponse.ProductVariantResponse.builder()
            .id(variant.getId())
            .sku(variant.getSku())
            .price(variant.getPrice())
            .attributes(variant.getAttributes()) // Trả về nguyên cục JSONB
            .width(variant.getWidth())
            .height(variant.getHeight())
            .depth(variant.getDepth())
            .weight(variant.getWeight())
            .build();
    }

    private ProductVariantListingResponse mapToVariantListing(
        ProductVariant variant
    ) {
        Product parent = variant.getProduct();

        // 1. Ảnh mặc định (Placeholder) nếu không tìm thấy gì cả
        String finalImageUrl = "https://placehold.co/600x400?text=No+Image";

        // 2. LOGIC TÌM ẢNH:
        // Ưu tiên 1: Lấy ảnh của chính Variant đó (nếu có)
        if (variant.getMedias() != null && !variant.getMedias().isEmpty()) {
            // Lấy cái đầu tiên tìm thấy
            finalImageUrl = variant.getMedias().get(0).getUrl();
        }
        // Ưu tiên 2: Nếu Variant không có ảnh riêng -> Lấy ảnh đại diện (Primary) của Product Cha
        else if (parent.getMedias() != null && !parent.getMedias().isEmpty()) {
            finalImageUrl = parent
                .getMedias()
                .stream()
                .filter(media -> Boolean.TRUE.equals(media.getIsPrimary())) // Tìm ảnh primary
                .findFirst()
                .map(ProductMedia::getUrl)
                .orElse(parent.getMedias().get(0).getUrl()); // Không có primary thì lấy đại cái đầu
        }

        return ProductVariantListingResponse.builder()
            .id(variant.getId())
            .productId(parent.getId())
            .name(parent.getName())
            .sku(variant.getSku())
            .price(variant.getPrice())
            .attributes(variant.getAttributes())
            .imageUrl(finalImageUrl) // Link ảnh chuẩn
            .build();
    }
}
