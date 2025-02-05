package com.fernandocanabarro.fullstack_ecommerce_app.projections;

import java.util.List;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Role;

public interface UserResponseProjection {

    Long getId();
    String getFullName();
    String getEmail();
    String getCpf();
    Boolean getActivated();
    List<Role> getRoles();
}
