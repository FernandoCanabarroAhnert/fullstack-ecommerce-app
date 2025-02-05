package com.fernandocanabarro.fullstack_ecommerce_app.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wish_lists")
@EqualsAndHashCode(of = "id")
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "wishList")
    private User user;

    @ManyToMany
    @JoinTable(name = "product_wish_list_items",
        joinColumns = @JoinColumn(name = "wish_list_id", foreignKey = @ForeignKey(name = "wish_list_fk", value = ConstraintMode.CONSTRAINT), nullable = false),
        inverseJoinColumns = @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "product_fk", value = ConstraintMode.CONSTRAINT), nullable = false))
    private Set<Product> items = new HashSet<>();

    public void addProduct(Product product) {
        this.items.add(product);
    }

    public void removeProduct(Product product) {
        this.items.remove(product);
    }
}
