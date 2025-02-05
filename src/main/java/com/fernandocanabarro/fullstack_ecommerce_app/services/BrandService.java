package com.fernandocanabarro.fullstack_ecommerce_app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.BrandRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.BrandResponseDTO;

public interface BrandService {

    List<BrandResponseDTO> findAll();
    Page<BrandResponseDTO> findAllPaginated(String name, Pageable pageable);
    BrandRequestDTO findByIdToUpdate(Long id);
    void addBrand(BrandRequestDTO dto);
    void updateBrand(Long id, BrandRequestDTO dto);
    void deleteBrand(Long id);

}
