package com.fernandocanabarro.fullstack_ecommerce_app.services.impl;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CategoryRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CategoryResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CategoryUpdateDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Category;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.CategoryRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.services.CategoryService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponseDTO> findAllPaginated(String name,Pageable pageable){
        return categoryRepository.findByNameContainingIgnoreCase(name,pageable)
            .map(CategoryResponseDTO::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll(){
        return categoryRepository.findAll().stream()
            .map(CategoryResponseDTO::new).toList();
    }

    @Override
    @Transactional
    public void createCategory(CategoryRequestDTO dto) {
        if (categoryRepository.findByNameIgnoreCase(dto.getName()).isPresent()) {
            throw new BadRequestException("Categoria com o nome " + dto.getName() + " já existe");
        }
        Category category = new Category();
        category.setName(dto.getName());

        String base64;
        try {
            String base64Prefix = "data:image/png;base64,";
            base64 = base64Prefix + Base64.getEncoder().encodeToString(dto.getImage().getBytes());
        }
        catch (IOException e) {
            throw new BadRequestException("Erro ao fazer upload da imagem");
        }

        category.setImage(base64);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public CategoryUpdateDTO findByIdToUpdate(Long id){
        return categoryRepository.findById(id)
            .map(CategoryUpdateDTO::new)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    @Override
    @Transactional
    public void updateCategory(Long id, CategoryUpdateDTO dto) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
        category.setName(dto.getName());
        String base64;
        if (!dto.getImage().isEmpty()) {
            try {
                String base64Prefix = "data:image/png;base64,";
                base64 = base64Prefix + Base64.getEncoder().encodeToString(dto.getImage().getBytes());
            }
            catch (IOException e) {
                throw new BadRequestException("Erro ao fazer upload da imagem");
            }
            category.setImage(base64);
        }
        categoryRepository.save(category);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }
        try {
            categoryRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Falha de Integridade - Categoria não pode ser deletada");
        }
    }
}
