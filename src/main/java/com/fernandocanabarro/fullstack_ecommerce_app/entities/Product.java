package com.fernandocanabarro.fullstack_ecommerce_app.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fernandocanabarro.fullstack_ecommerce_app.projections.IdProjection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "products")
@EqualsAndHashCode(of = "id")
public class Product implements IdProjection<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, columnDefinition = "text")
    private String description;
    @Column(nullable = false)
    private BigDecimal price;

    private Integer views;

    private Integer stockQuantity;

    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ProductImage> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "product_category",
        joinColumns = @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "product_fk", value = ConstraintMode.CONSTRAINT), nullable = false),
        inverseJoinColumns = @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "category_fk", value = ConstraintMode.CONSTRAINT), nullable = false))
    private List<Category> categories = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "brand_id",foreignKey = @ForeignKey(name = "brand_fk", value = ConstraintMode.CONSTRAINT))
    private Brand brand;

    private Boolean isInOffer;
    private BigDecimal offerDiscountPercentage;
    private LocalDateTime offerExpirationDateTime;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ProductRating> productRatings = new ArrayList<>();

    @OneToMany(mappedBy = "id.product", fetch = FetchType.LAZY)
    private List<ProductOrderItem> orders = new ArrayList<>();

    @OneToMany(mappedBy = "id.product", fetch = FetchType.LAZY)
    private List<ProductCartItem> carts = new ArrayList<>();

    public void increaseStockQuantity(int quantity) {
        this.stockQuantity += quantity;
    }

    public void decreaseStockQuantity(int quantity) {
        this.stockQuantity -= quantity;
    }

    public BigDecimal getOfferPrice() {
        return this.price.multiply(BigDecimal.valueOf(1.0)
                .subtract(this.offerDiscountPercentage.divide(BigDecimal.valueOf(100.0))))
                .setScale(2, RoundingMode.HALF_UP);
    }

}
