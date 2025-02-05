package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String image;

    public CategoryResponseDTO(Category entity) {
        id = entity.getId();
        name = entity.getName();
        image = entity.getImage();
    }
}
