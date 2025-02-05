package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import org.springframework.web.multipart.MultipartFile;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateDTO {

    private Long id;
    @NotBlank(message = "Campo Requerido")
    private String name;
    private MultipartFile image;

    public CategoryUpdateDTO(Category entity) {
        id = entity.getId();
        name = entity.getName();
    }
}
