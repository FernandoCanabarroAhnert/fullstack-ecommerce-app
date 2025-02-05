package com.fernandocanabarro.fullstack_ecommerce_app.services;

import java.util.List;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CreditCardRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CreditCardResponseDTO;

public interface CreditCardService {

    List<CreditCardResponseDTO> getConnectedUserCreditCards();
    void createCreditCard(CreditCardRequestDTO dto);
    void deleteCreditCard(Long id);
}
