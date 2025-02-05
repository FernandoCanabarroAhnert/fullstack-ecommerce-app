package com.fernandocanabarro.fullstack_ecommerce_app.entities;

import java.math.BigDecimal;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.enums.PaymentStatus;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.enums.PaymentType;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(of = "id")
public abstract class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @OneToOne
    @JoinColumn(name = "order_id",foreignKey = @ForeignKey(name = "order_fk", value = ConstraintMode.CONSTRAINT))
    private Order order;

    private BigDecimal totalValue;
}
