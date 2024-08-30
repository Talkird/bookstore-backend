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
        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        Book book = bookRepository.findById(bookId)
                                  .orElseThrow(() -> new RuntimeException("Book not found"));

        Optional<CartItem> existingCartItem = cart.getBooks()
                                                  .stream()
                                                  .filter(item -> item.getBook().getId().equals(bookId))
                                                  .findFirst();

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setQuantity(1);
            cart.getBooks().add(cartItem);
        }

        cartItem.updatePrice();

        cart.updateTotal();

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
        CartItem existingCartItem = cartItemRepository.findById(cartItem.getId())
            .orElseThrow(() -> new RuntimeException("CartItem not found"));

        existingCartItem.setQuantity(cartItem.getQuantity());
        existingCartItem.setPrice(existingCartItem.getBook().getPrice() * cartItem.getQuantity());

        cartItemRepository.save(existingCartItem);

        Cart cart = existingCartItem.getCart();
        cart.updateTotal();
        cartRepository.save(cart);

        return existingCartItem;
    }
    @Override
    public void deleteCartItem(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("CartItem not found"));

        Cart cart = cartItem.getCart();

        cart.getBooks().remove(cartItem);

        cart.updateTotal();

        cartItemRepository.delete(cartItem);

        cartRepository.save(cart);
    }
}
