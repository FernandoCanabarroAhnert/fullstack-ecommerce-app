package com.fernandocanabarro.fullstack_ecommerce_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{

    Role findByAuthority(String authority);
}
