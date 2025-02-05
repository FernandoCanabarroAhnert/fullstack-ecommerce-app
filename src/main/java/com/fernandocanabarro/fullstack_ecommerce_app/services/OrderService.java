package com.fernandocanabarro.fullstack_ecommerce_app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductOrderItemResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.OrderRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.OrderResponseDTO;

public interface OrderService {

    ProductOrderItemResponseDTO getOrderItemResponseDTOFromRequest(Long productId,int quantity);
    List<ProductOrderItemResponseDTO> convertCartItemsToOrderItemResponseDTO();
    void createOrderFromCart(OrderRequestDTO dto);
    void createOrderFromProductRequest(Long productId, int quantity, OrderRequestDTO dto);

    List<OrderResponseDTO> adminFindAll();
    Page<OrderResponseDTO> adminFindAllOrdersPaginated(Pageable pageable, String minDate, String maxDate);

    List<OrderResponseDTO> getConnectedUserOrders();
}
