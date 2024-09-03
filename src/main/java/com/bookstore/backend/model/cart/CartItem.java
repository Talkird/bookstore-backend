package com.bookstore.backend.model.cart;

import com.bookstore.backend.model.book.Book;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column
    private int quantity;

    @Column
    private double price;

    public void updatePrice() {
        this.price = this.book.getPrice() * this.quantity;
    }
}