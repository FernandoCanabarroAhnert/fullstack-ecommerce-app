package com.fernandocanabarro.fullstack_ecommerce_app.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.OrderRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.OrderResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductOrderItemResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Address;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.BoletoPayment;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.CreditCard;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.CreditCardPayment;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.DiscountCoupon;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Order;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.PixPayment;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Product;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.ProductCartItem;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.ProductOrderItem;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.UserDiscountCoupon;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.enums.OrderStatus;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.enums.PaymentStatus;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.enums.PaymentType;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.pk.ProductCartItemPK;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.pk.UserDiscountCouponPK;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.AddressRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.CreditCardRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.DiscountCouponRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.OrderRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.PaymentRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.ProductCartItemRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.ProductOrderItemRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.ProductRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.UserDiscountCouponRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.EmailService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.OrderService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final ProductOrderItemRepository productOrderItemRepository;
    private final CreditCardRepository creditCardRepository;
    private final PaymentRepository paymentRepository;
    private final ProductCartItemRepository productCartItemRepository;
    private final DiscountCouponRepository discountCouponRepository;
    private final UserDiscountCouponRepository userDiscountCouponRepository;
    private final AuthService authService;
    private final EmailService emailService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductOrderItemResponseDTO> convertCartItemsToOrderItemResponseDTO() {
        User user = authService.getConnectedUser();
        List<ProductOrderItemResponseDTO> response = new ArrayList<>();
        for (ProductCartItem item : user.getCart().getItems()) {
            Product product = productRepository.findById(item.getId().getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
            ProductOrderItemResponseDTO dto = new ProductOrderItemResponseDTO();
            dto.setProductId(product.getId());
            dto.setName(product.getName());
            dto.setImage(product.getImages().get(0).getImage());
            dto.setPrice(product.getIsInOffer() ? String.valueOf(product.getOfferPrice()) : String.valueOf(product.getPrice()));
            dto.setQuantity(item.getQuantity());
            response.add(dto);
        }
        return response;
    }

    @Override
    @Transactional
    public void createOrderFromCart(OrderRequestDTO dto) {
        User user = authService.getConnectedUser();
        Address address = addressRepository.findById(dto.getAddressId())
            .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));
        Order order = new Order();
        order.setUser(user);
        order.setMoment(LocalDateTime.now());
        order.setAddress(address);

        for (ProductCartItem item : user.getCart().getItems()) {
            Product product = productRepository.findById(item.getId().getProduct().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
            ProductOrderItem productOrderItem = new ProductOrderItem(product,order,item.getQuantity(),
                product.getIsInOffer() ? product.getOfferPrice() : product.getPrice());
            order.getItems().add(productOrderItem);
        }

        orderRepository.save(order);
        productOrderItemRepository.saveAll(order.getItems());

        setOrderPayment(order, dto);

        if (dto.getDiscountCouponId() != null) {
            DiscountCoupon discountCoupon = discountCouponRepository.findById(dto.getDiscountCouponId())
                .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado"));
            UserDiscountCouponPK userDiscountCouponPk = new UserDiscountCouponPK(discountCoupon, user);
            UserDiscountCoupon userDiscountCoupon = userDiscountCouponRepository.findById(userDiscountCouponPk)
                .orElseThrow(() -> new ResourceNotFoundException("O Usuário não possui o Cupom selecionado"));
            BigDecimal discountPrice = order.getTotalValue().multiply(discountCoupon.getDiscountPercentage()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
            order.getPayment().setTotalValue(order.getTotalValue().subtract(discountPrice));
            userDiscountCoupon.setIsUsed(true);
            userDiscountCoupon.setUsedAt(LocalDateTime.now());
            userDiscountCouponRepository.save(userDiscountCoupon);
            order.setDiscountCoupon(discountCoupon);
        }

        orderRepository.save(order);
        paymentRepository.save(order.getPayment());
        productOrderItemRepository.saveAll(order.getItems());

        user.getCart().getItems().forEach(item -> {
            Product product = productRepository.findById(item.getId().getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
            productCartItemRepository.deleteById(new ProductCartItemPK(product, user.getCart()));
        });
        emailService.sendOrderSummaryEmail(user.getFullName(), user.getEmail(), order.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductOrderItemResponseDTO getOrderItemResponseDTOFromRequest(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        ProductOrderItemResponseDTO dto = new ProductOrderItemResponseDTO();
        dto.setProductId(product.getId());
        dto.setName(product.getName());
        dto.setImage(product.getImages().get(0).getImage());
        dto.setPrice(product.getIsInOffer() ? String.valueOf(product.getOfferPrice()) : String.valueOf(product.getPrice()));
        dto.setQuantity(quantity);
        return dto;
    }

    @Override
    @Transactional
    public void createOrderFromProductRequest(Long productId, int quantity, OrderRequestDTO dto) {
        User user = authService.getConnectedUser();
        Address address = addressRepository.findById(dto.getAddressId())
            .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));
        Order order = new Order();
        order.setUser(user);
        order.setMoment(LocalDateTime.now());
        order.setAddress(address);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        ProductOrderItem productOrderItem = new ProductOrderItem(product,order,quantity,
            product.getIsInOffer() ? product.getOfferPrice() : product.getPrice());
        order.getItems().add(productOrderItem);

        orderRepository.save(order);
        productOrderItemRepository.saveAll(order.getItems());

        setOrderPayment(order, dto);

        if (dto.getDiscountCouponId() != null) {
            DiscountCoupon discountCoupon = discountCouponRepository.findById(dto.getDiscountCouponId())
                .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado"));
            UserDiscountCouponPK userDiscountCouponPk = new UserDiscountCouponPK(discountCoupon, user);
            UserDiscountCoupon userDiscountCoupon = userDiscountCouponRepository.findById(userDiscountCouponPk)
                .orElseThrow(() -> new ResourceNotFoundException("O Usuário não possui o Cupom selecionado"));
            BigDecimal discountPrice = order.getTotalValue().multiply(discountCoupon.getDiscountPercentage()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
            order.getPayment().setTotalValue(order.getTotalValue().subtract(discountPrice));
            userDiscountCoupon.setIsUsed(true);
            userDiscountCoupon.setUsedAt(LocalDateTime.now());
            userDiscountCouponRepository.save(userDiscountCoupon);
            order.setDiscountCoupon(discountCoupon);
        }

        orderRepository.save(order);
        paymentRepository.save(order.getPayment());
        productOrderItemRepository.saveAll(order.getItems());
        emailService.sendOrderSummaryEmail(user.getFullName(), user.getEmail(), order.getId());
    }

    private void setOrderPayment(Order order,OrderRequestDTO dto) {
        switch (dto.getPayment().getPaymentType()) {
            case "BOLETO":
                order.setOrderStatus(OrderStatus.WAITING_PAYMENT);
                BoletoPayment boletoPayment = new BoletoPayment();
                boletoPayment.setPaymentType(PaymentType.BOLETO);
                boletoPayment.setPaymentStatus(PaymentStatus.PENDING);
                boletoPayment.setTotalValue(order.getTotalValue());
                boletoPayment.setOrder(order);
                boletoPayment.setExpiresAt(LocalDate.now().plusDays(30L));
                boletoPayment.setPaidAt(null);
                order.setPayment(boletoPayment);
                break;
            case "PIX":
                order.setOrderStatus(OrderStatus.PAID);
                PixPayment pixPayment = new PixPayment();
                pixPayment.setPaymentType(PaymentType.PIX);
                pixPayment.setPaymentStatus(PaymentStatus.PAID);
                pixPayment.setTotalValue(order.getTotalValue());
                pixPayment.setOrder(order);
                pixPayment.setPaidAt(LocalDateTime.now());
                order.setPayment(pixPayment);
                break;
            case "CARTAO":
                CreditCard creditCard = creditCardRepository.findById(dto.getPayment().getCreditCardId())
                .orElseThrow(() -> new ResourceNotFoundException("Cartão de Crédito não encontrado"));
                order.setOrderStatus(OrderStatus.WAITING_PAYMENT);
                CreditCardPayment creditCardPayment = new CreditCardPayment();
                creditCardPayment.setPaymentType(PaymentType.CARTAO);
                creditCardPayment.setPaymentStatus(PaymentStatus.PENDING);
                creditCardPayment.setTotalValue(order.getTotalValue());
                creditCardPayment.setOrder(order);
                creditCardPayment.setCreditCard(creditCard);
                creditCardPayment.setInstallmentsQuantity(dto.getPayment().getInstallmentQuantity());
                order.setPayment(creditCardPayment);
                break;
        }
    }

    @Override
    @Transactional
    public List<OrderResponseDTO> adminFindAll() {
        return orderRepository.findAll().stream()
            .map(OrderResponseDTO::new).toList();
    }

    @Override
    @Transactional
    public Page<OrderResponseDTO> adminFindAllOrdersPaginated(Pageable pageable, String minDate, String maxDate) {
        LocalDateTime min = !minDate.equals("") 
            ? LocalDateTime.of(Integer.parseInt(minDate.substring(0,4)), 
                            Integer.parseInt(minDate.substring(5, 7)), 
                            Integer.parseInt(minDate.substring(8)),
                            0, 0)
            : LocalDateTime.now().minusYears(1L);

        LocalDateTime max = !maxDate.equals("") 
            ? LocalDateTime.of(Integer.parseInt(maxDate.substring(0,4)),
                            Integer.parseInt(maxDate.substring(5, 7)),
                            Integer.parseInt(maxDate.substring(8)),
                            23, 59) 
            : LocalDateTime.now();
        
        return orderRepository.findByMomentBetween(min, max, pageable).map(OrderResponseDTO::new);
    }

    @Override
    @Transactional
    public List<OrderResponseDTO> getConnectedUserOrders() {
        return authService.getConnectedUser().getOrders()
            .stream().map(OrderResponseDTO::new).toList();
    }

}
