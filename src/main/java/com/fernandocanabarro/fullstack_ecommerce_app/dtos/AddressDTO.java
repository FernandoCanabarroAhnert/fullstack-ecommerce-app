package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Address;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private Long id;
    @NotBlank(message = "Campo Requerido")
    private String cep;
    @NotBlank(message = "Campo Requerido")
    private String logradouro;
    @NotBlank(message = "Campo Requerido")
    private String numero;
    @NotBlank(message = "Campo Requerido")
    private String bairro;
    @NotBlank(message = "Campo Requerido")
    private String cidade;
    @NotBlank(message = "Campo Requerido")
    private String estado;

    public AddressDTO(Address entity) {
        id = entity.getId();
        cep = entity.getCep();
        logradouro = entity.getLogradouro();
        numero = entity.getNumero();
        bairro = entity.getBairro();
        cidade = entity.getCidade();
        estado = entity.getEstado();
    }
}
