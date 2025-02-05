package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.ProductCartItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCartItemResponseDTO {

    private Long productId;
    private String image;
    private String name;
    private String price;
    private Integer quantity;

    public ProductCartItemResponseDTO(ProductCartItem entity) {
        productId = entity.getId().getProduct().getId();
        image = entity.getId().getProduct().getImages().get(0).getImage();
        name = entity.getId().getProduct().getName();
        price = String.valueOf(entity.getPrice().setScale(2, RoundingMode.HALF_UP)).replace(".", ",");
        quantity = entity.getQuantity();
    }

    public String getSubTotal() {
        return String.valueOf(BigDecimal.valueOf(Double.parseDouble(price.replace(",", "."))).multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP)).replace(".", ",");
    }
}
