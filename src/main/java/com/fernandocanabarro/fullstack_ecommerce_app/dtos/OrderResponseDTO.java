package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private Long id;
    private String userEmail;
    private String moment;
    private String status;
    private String totalValue;
    private String discountPercentage;
    private String paymentType;
    private AddressDTO address;
    private List<ProductOrderItemResponseDTO> items;

    public OrderResponseDTO(Order entity) {
        id = entity.getId();
        userEmail = entity.getUser().getEmail();
        moment = entity.getMoment().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        status = switch (entity.getOrderStatus().toString()) {
            case "WAITING_PAYMENT" -> "Aguardando Pagamento";
            case "PAID" -> "Pago";
            case "SHIPPED" -> "Enviado para Transportadora";
            case "DELIVERED" -> "Entregue";
            case "CANCELED" -> "Cancelado";
            default -> null;
        };
        totalValue = String.valueOf(entity.getPayment().getTotalValue()).replace(".", ",");
        discountPercentage = entity.getDiscountCoupon() != null
            ? entity.getDiscountCoupon().getDiscountPercentage() + "%"
            : "0.0";
        paymentType = entity.getPayment().getPaymentType().toString();
        address = new AddressDTO(entity.getAddress());
        items = entity.getItems().stream().map(item -> new ProductOrderItemResponseDTO(item)).toList();
    }
}
