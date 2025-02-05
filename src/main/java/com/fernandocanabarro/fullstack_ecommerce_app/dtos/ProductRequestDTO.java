package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Category;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "Campo Requerido")
    private String name;
    @NotBlank(message = "Campo Requerido")
    private String description;
    @NotBlank(message = "Campo Requerido")
    private String price;
    private Long brand;
    private Boolean isInOffer;
    private BigDecimal offerDiscountPercentage;
    private LocalDateTime offerExpirationDateTime;
    @Min(value = 1, message = "Produto deve ter pelo menos 1 item em estoque")
    private Integer stockQuantity;
    private List<MultipartFile> images = new ArrayList<>();
    private List<Long> categories = new ArrayList<>();

    public ProductRequestDTO(Product entity) {
        name = entity.getName();
        description = entity.getDescription();
        String productPrice = String.valueOf(entity.getPrice()).replace(".", ",");
        price = productPrice;
        brand = entity.getBrand().getId();
        isInOffer = entity.getIsInOffer();
        offerDiscountPercentage = entity.getOfferDiscountPercentage();
        stockQuantity = entity.getStockQuantity();
        categories = entity.getCategories().stream().map(Category::getId).toList();
    }
}
