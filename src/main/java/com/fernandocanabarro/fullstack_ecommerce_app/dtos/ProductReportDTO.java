package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import java.util.stream.Collectors;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Category;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductReportDTO {

    private Long id;
    private String name;
    private String description;
    private String price;
    private String brand;
    private Integer stockQuantity;
    private String categories;

    public ProductReportDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        String productPrice = String.valueOf(entity.getPrice()).replace(".", ",");
        price = productPrice;
        brand = entity.getBrand().getName();
        stockQuantity = entity.getStockQuantity();
        categories = entity.getCategories().stream().map(Category::getName).collect(Collectors.joining(","));
    }
}
