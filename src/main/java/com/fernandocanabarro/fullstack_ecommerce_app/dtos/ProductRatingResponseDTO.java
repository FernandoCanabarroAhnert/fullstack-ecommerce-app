package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.ProductRating;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRatingResponseDTO {

    private String fullName;
    private String email;
    private String description;
    private String rating;

    public ProductRatingResponseDTO(ProductRating entity) {
        fullName = entity.getUser().getFullName();
        email = entity.getUser().getEmail();
        description = entity.getDescription();
        rating = String.format("%.1f",entity.getRating());
    }
}
