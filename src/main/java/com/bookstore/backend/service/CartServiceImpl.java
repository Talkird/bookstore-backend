package com.bookstore.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.backend.model.Book;
import com.bookstore.backend.model.Cart;
import com.bookstore.backend.model.CartItem;
import com.bookstore.backend.repository.BookRepository;
import com.bookstore.backend.repository.CartItemRepository;
import com.bookstore.backend.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    public CartItem addItemToCart(Long userId, Long bookId) {
        // Find the cart for the user
        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        // Find the book
        Book book = bookRepository.findById(bookId)
                                  .orElseThrow(() -> new RuntimeException("Book not found"));

        // Check if the book is already in the cart
        Optional<CartItem> existingCartItem = cart.getBooks()
                                                  .stream()
                                                  .filter(item -> item.getBook().getId().equals(bookId))
                                                  .findFirst();

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            // If the book is already in the cart, increment the quantity
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            // If the book is not in the cart, create a new CartItem
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setQuantity(1);
            cart.getBooks().add(cartItem);
        }

        // Update the price based on the quantity
        cartItem.updatePrice();

        // Update the cart total
        cart.updateTotal();

        // Save cart item and cart
        cartRepository.save(cart);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public List<CartItem> getCart(Long userId) {
        return cartRepository.findByUserId(userId).get().getBooks();
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).get();
        cart.getBooks().clear();
        cart.updateTotal();

    }

    @Override
    public CartItem updateCartItem(CartItem cartItem) {
        // Find the cart item in the database
        CartItem existingCartItem = cartItemRepository.findById(cartItem.getId())
            .orElseThrow(() -> new RuntimeException("CartItem not found"));

        // Update the quantity and price
        existingCartItem.setQuantity(cartItem.getQuantity());
        existingCartItem.setPrice(existingCartItem.getBook().getPrice() * cartItem.getQuantity());

        // Save the updated cart item
        cartItemRepository.save(existingCartItem);

        // Update the cart total
        Cart cart = existingCartItem.getCart();
        cart.updateTotal();
        cartRepository.save(cart);

        return existingCartItem;
    }
    @Override
    public void deleteCartItem(Long id) {
        // Find the cart item in the database
        CartItem cartItem = cartItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("CartItem not found"));

        // Get the associated cart
        Cart cart = cartItem.getCart();

        // Remove the cart item from the cart
        cart.getBooks().remove(cartItem);

        // Update the cart total
        cart.updateTotal();

        // Delete the cart item
        cartItemRepository.delete(cartItem);

        // Save the updated cart
        cartRepository.save(cart);
    }
}
