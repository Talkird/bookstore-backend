package com.bookstore.backend.model.cart;

import java.util.List;

import com.bookstore.backend.model.user.User;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> books;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private double total;

    public void updateTotal() {
        this.total = 0;
        for (CartItem cartItem : books) {
            total += cartItem.getPrice() * cartItem.getQuantity();
        }
    }

}
