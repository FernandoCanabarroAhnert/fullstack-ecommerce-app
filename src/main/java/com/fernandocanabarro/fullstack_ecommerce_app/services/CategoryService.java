package com.fernandocanabarro.fullstack_ecommerce_app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CategoryRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CategoryResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CategoryUpdateDTO;

public interface CategoryService {

    List<CategoryResponseDTO> findAll();
    Page<CategoryResponseDTO> findAllPaginated(String name,Pageable pageable);
    void createCategory(CategoryRequestDTO dto);
    CategoryUpdateDTO findByIdToUpdate(Long id);
    void updateCategory(Long id, CategoryUpdateDTO dto); 
    void deleteCategory(Long id);
}
