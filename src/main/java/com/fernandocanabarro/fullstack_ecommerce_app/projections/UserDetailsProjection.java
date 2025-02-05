package com.fernandocanabarro.fullstack_ecommerce_app.projections;

public interface UserDetailsProjection {

    String getUsername();
    String getPassword();
    Boolean getActivated();
    Long getRoleId();
    String getAuthority();
}
