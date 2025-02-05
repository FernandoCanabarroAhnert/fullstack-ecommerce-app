package com.fernandocanabarro.fullstack_ecommerce_app.services.impl;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.AddressDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Address;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.AddressRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AddressService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public List<AddressDTO> findConnectedUserAddresses() {
        return authService.getConnectedUser().getAddresses()
            .stream().map(AddressDTO::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDTO findById(Long id) {
        return addressRepository.findById(id)
            .map(AddressDTO::new)
            .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado")); 
    }

    @Override
    @Transactional
    public void addAddress(AddressDTO dto) {
        User user = authService.getConnectedUser();
        Address address = new Address();
        address.setCep(dto.getCep());
        address.setLogradouro(dto.getLogradouro());
        address.setNumero(dto.getNumero());
        address.setBairro(dto.getBairro());
        address.setCidade(dto.getCidade());
        address.setEstado(dto.getEstado());
        address.setUser(user);
        addressRepository.save(address);
    }

    @Override
    @Transactional
    public void updateAddress(Long id, AddressDTO dto) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));
        address.setCep(dto.getCep());
        address.setLogradouro(dto.getLogradouro());
        address.setNumero(dto.getNumero());
        address.setBairro(dto.getBairro());
        address.setCidade(dto.getCidade());
        address.setEstado(dto.getEstado());
        addressRepository.save(address);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Endereço não encontrado");
        }
        try {
            addressRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Falha de Integridade - Endereço não pode ser deletado");
        }
    }
}
