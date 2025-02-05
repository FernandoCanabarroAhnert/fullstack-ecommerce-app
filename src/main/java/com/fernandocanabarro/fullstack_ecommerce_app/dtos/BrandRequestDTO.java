package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import org.springframework.web.multipart.MultipartFile;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Brand;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequestDTO {

    @NotBlank(message = "Campo Requerido")
    private String name;
    private MultipartFile image;

    public BrandRequestDTO(Brand entity) {
        name = entity.getName();
    }
}
