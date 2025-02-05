package com.fernandocanabarro.fullstack_ecommerce_app.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
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
@Table(name = "boleto_payments")
public class BoletoPayment extends Payment {

    private LocalDate expiresAt;
    private LocalDate paidAt;
}
