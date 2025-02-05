package com.fernandocanabarro.fullstack_ecommerce_app.entities;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "credit_card_payments")
public class CreditCardPayment extends Payment {

    @ManyToOne
    @JoinColumn(name = "credit_card_id", foreignKey = @ForeignKey(name = "credit_card_fk", value = ConstraintMode.CONSTRAINT))
    private CreditCard creditCard;
    private Integer installmentsQuantity;

}
