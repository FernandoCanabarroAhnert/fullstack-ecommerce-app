package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRatingRequestDTO {

    private String description;
    @NotNull(message = "Campo Requerido")
    private Double rating;
}
