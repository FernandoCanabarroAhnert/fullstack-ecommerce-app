package com.fernandocanabarro.fullstack_ecommerce_app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.ActivationCode;

@Repository
public interface ActivationCodeRepository extends JpaRepository<ActivationCode,Long>{

    Optional<ActivationCode> findByCode(String code);
}
