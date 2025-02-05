package com.fernandocanabarro.fullstack_ecommerce_app.services;

import java.util.List;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.AddressDTO;

public interface AddressService {

    List<AddressDTO> findConnectedUserAddresses();
    AddressDTO findById(Long id);
    void addAddress(AddressDTO dto);
    void updateAddress(Long id, AddressDTO dto);
    void deleteAddress(Long id);
}
