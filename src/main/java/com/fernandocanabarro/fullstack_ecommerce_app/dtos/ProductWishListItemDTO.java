package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import java.math.RoundingMode;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductWishListItemDTO {

    private Long productId;
    private String image;
    private String name;
    private String description;
    private String price;

    public ProductWishListItemDTO(Product entity){
        productId = entity.getId();
        image = entity.getImages().get(0).getImage();
        name = entity.getName();
        description = entity.getDescription();
        price = String.valueOf(entity.getPrice().setScale(2, RoundingMode.HALF_UP)).replace(".", ",");
    }
}
