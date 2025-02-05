package com.fernandocanabarro.fullstack_ecommerce_app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductDetailDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductGridDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductImageDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductRatingRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductRatingResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductReportDTO;

public interface ProductService {

    List<ProductReportDTO> findAllForReport();
    Page<ProductReportDTO> findAllForReportPaginated(String name,Pageable pageable);

    ProductRequestDTO findProductByIdToUpdate(Long id);

    void createProduct(ProductRequestDTO dto);
    void updateProduct(Long id,ProductRequestDTO dto);
    void deleteProduct(Long id);

    void increaseProductStockQuantity(Long id, Integer quantity);
    void decreaseProductStockQuantity(Long id, Integer quantity);

    List<ProductImageDTO> findProductImagesByProductId(Long productId);
    void deleteProductImageById(Long id);

    Page<ProductGridDTO> findAllForGridPaginated(String name,Pageable pageable);
    ProductDetailDTO findByIdForProductDetail(Long id);

    Page<ProductGridDTO> findAllForGridPaginatedAndFiltered(List<Long> categoriesIds, List<Long> brandsIds,Integer minPrice, Integer maxPrice, String name,  Pageable pageable);

    void rateProduct(Long productId,ProductRatingRequestDTO dto);
    List<ProductRatingResponseDTO> getProductRatingsByProductId(Long id);

    List<ProductGridDTO> getProductsInOffer();
}
