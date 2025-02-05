package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import java.math.BigDecimal;
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
public class ProductGridDTO {

    private Long id;
    private String name;
    private String price;
    private Double averageRating;
    private Boolean isInOffer;
    private String discountPercentage;
    private String offerPrice;
    private String categories;
    private String image;

    public ProductGridDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        price = String.valueOf(entity.getPrice()).replace(".", ",");
        averageRating = entity.getProductRatings().stream().mapToDouble(ProductRating::getRating).average().orElse(0.0);
        isInOffer = entity.getIsInOffer();
        if (isInOffer) {
            discountPercentage = (entity.getOfferDiscountPercentage().compareTo(BigDecimal.valueOf(10)) == 1 || entity.getOfferDiscountPercentage().compareTo(BigDecimal.valueOf(10)) == 0)
                ? String.valueOf(entity.getOfferDiscountPercentage()).substring(0,2)
                : String.valueOf(entity.getOfferDiscountPercentage()).substring(0,1);
            offerPrice = String.valueOf(entity.getOfferPrice());
        }
        else {
            discountPercentage = null;
            offerPrice = null;
        }

        categories = entity.getCategories().stream().map(Category::getName).collect(Collectors.joining(","));
        image = !entity.getImages().isEmpty() ? entity.getImages().get(0).getImage() : "";
    }
}
