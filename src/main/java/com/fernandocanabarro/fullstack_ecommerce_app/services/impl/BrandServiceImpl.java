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

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.BrandRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.BrandResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Brand;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.BrandRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.services.BrandService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponseDTO> findAll() {
        return brandRepository.findAll()
            .stream().map(BrandResponseDTO::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BrandResponseDTO> findAllPaginated(String name,Pageable pageable) {
        return brandRepository.findByNameContainingIgnoreCase(name, pageable)
            .map(BrandResponseDTO::new);
    }

    @Override
    @Transactional(readOnly = true)
    public BrandRequestDTO findByIdToUpdate(Long id) {
        return brandRepository.findById(id)
            .map(BrandRequestDTO::new)
            .orElseThrow(() -> new ResourceNotFoundException("Marca de Produto não encontrada"));
    }

    @Override
    @Transactional
    public void addBrand(BrandRequestDTO dto) {
        Brand brand = new Brand();
        brand.setName(dto.getName());
        String base64;
        try {
            String base64Prefix = "data:image/png;base64,";
            base64 = base64Prefix + Base64.getEncoder().encodeToString(dto.getImage().getBytes());
        } catch (IOException e) {
            throw new BadRequestException("Erro ao fazer upload da imagem");
        }
        brand.setImage(base64);
        brandRepository.save(brand);
    }

    @Override
    @Transactional
    public void updateBrand(Long id, BrandRequestDTO dto) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca de Produto não encontrada"));
        brand.setName(dto.getName());
        if (!dto.getImage().isEmpty()) {
            String base64;
            try {
                String base64Prefix = "data:image/png;base64,";
                base64 = base64Prefix + Base64.getEncoder().encodeToString(dto.getImage().getBytes());
            } catch (IOException e) {
                throw new BadRequestException("Erro ao fazer upload da imagem");
            }
            brand.setImage(base64);
        }
        brandRepository.save(brand);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteBrand(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new ResourceNotFoundException("Marca de Produto não encontrada");
        }
        try {
            brandRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Falha de Integridade - Marca não pode ser deletada");
        }
    }

}
