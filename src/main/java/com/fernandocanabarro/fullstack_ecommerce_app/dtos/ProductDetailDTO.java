package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import java.util.List;
import java.util.stream.Collectors;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Category;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Product;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.ProductRating;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDTO {

    private Long id;
    private String name;
    private Double averageRating;
    private String averageRatingResponse;
    private String description;
    private String price;
    private Integer soldQuantity;
    private Boolean isInOffer;
    private String discountPercentage;
    private String offerPrice;
    private BrandResponseDTO brand;
    private String categories;
    private List<ProductImageDTO> images;

    public ProductDetailDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        averageRating = entity.getProductRatings().stream().mapToDouble(ProductRating::getRating).average().orElse(0.0);
        averageRatingResponse = String.format("%.1f",averageRating);
        description = entity.getDescription();
        price = String.valueOf(entity.getPrice()).replace(".", ",");
        soldQuantity = entity.getOrders().size();
        isInOffer = entity.getIsInOffer();
        if (isInOffer) {
            discountPercentage = String.valueOf(entity.getOfferDiscountPercentage());
            offerPrice = String.valueOf(entity.getOfferPrice());
        }
        else {
            discountPercentage = null;
            offerPrice = null;
        }
        brand = new BrandResponseDTO(entity.getBrand());
        categories = entity.getCategories().stream().map(Category::getName).collect(Collectors.joining(","));
        images = entity.getImages().stream().map(ProductImageDTO::new).toList();
    }
}
