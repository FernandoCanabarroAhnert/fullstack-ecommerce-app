package com.fernandocanabarro.fullstack_ecommerce_app.entities;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "activation_codes")
@EqualsAndHashCode(of = "id")
public class ActivationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @ManyToOne
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "user_fk", value = ConstraintMode.CONSTRAINT), nullable = false)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime validatedAt;

    public ActivationCode(String code, User user) {
        this.code = code;
        this.user = user;
        expiresAt = LocalDateTime.now().plusMinutes(30L);
    }

    public boolean isValid() {
        return LocalDateTime.now().isBefore(expiresAt);
    }
}
