package com.fernandocanabarro.fullstack_ecommerce_app.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CreditCardRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CreditCardResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.CreditCard;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.enums.CreditCardBrand;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.CreditCardRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.CreditCardService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public List<CreditCardResponseDTO> getConnectedUserCreditCards() {
        return authService.getConnectedUser().getCreditCards()
            .stream().map(CreditCardResponseDTO::new).toList();
    }

    @Override
    @Transactional
    public void createCreditCard(CreditCardRequestDTO dto) {
        User user = authService.getConnectedUser();
        CreditCard creditCard = new CreditCard();
        creditCard.setUser(user);
        creditCard.setHolderName(dto.getHolderName());
        creditCard.setCardNumber(dto.getCardNumber());
        creditCard.setCvv(dto.getCvv());
        String year = dto.getExpirationDate().substring(0,4);
        String month = dto.getExpirationDate().substring(5);
        creditCard.setExpirationDate(LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1));
        creditCard.setBrand(CreditCardBrand.valueOf(dto.getBrand()));
        creditCardRepository.save(creditCard);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteCreditCard(Long id) {
        if (!creditCardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cartão de Crédito não encontrado");
        }
        try {
            creditCardRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Falha de Integridade - Cartão de Crédito não pode ser deletado");
        }
    }


}
