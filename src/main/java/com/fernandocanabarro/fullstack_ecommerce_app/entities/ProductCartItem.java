package com.fernandocanabarro.fullstack_ecommerce_app.entities;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.pk.ProductCartItemPK;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_cart_items")
public class ProductCartItem {

    @EmbeddedId
    private ProductCartItemPK id = new ProductCartItemPK();

    private Integer quantity;

    private BigDecimal price;

    public ProductCartItem(Product product, Cart cart, Integer quantity, BigDecimal price) {
        id.setProduct(product);
        id.setCart(cart);
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductCartItem other = (ProductCartItem) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
}
