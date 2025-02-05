package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.CreditCard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardResponseDTO {

    private Long id;
    private String holderName;
    private String cardNumber;
    private String expirationDate;
    private String brand;

    public CreditCardResponseDTO(CreditCard entity) {
        id = entity.getId();
        holderName = entity.getHolderName();
        cardNumber = "**** **** **** " + entity.getCardNumber().substring(12);
        expirationDate = String.valueOf(entity.getExpirationDate().getMonth().getValue()) + "/" + String.valueOf(entity.getExpirationDate().getYear());
        brand = entity.getBrand().toString();
    }
}
