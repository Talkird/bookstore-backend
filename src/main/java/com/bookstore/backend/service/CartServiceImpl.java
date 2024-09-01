package com.bookstore.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.backend.exceptions.BookNotFoundException;
import com.bookstore.backend.exceptions.CartItemNotFoundException;
import com.bookstore.backend.exceptions.CartNotFoundException;
import com.bookstore.backend.exceptions.InvalidBookDataException;
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

    @Override
    public List<CartItem> getCart(Long userId) throws CartNotFoundException {
        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));
        return cart.getBooks();
    }

    @Override
    public void clearCart(Long userId) throws CartNotFoundException {
        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));
        cart.getBooks().clear();
        cart.updateTotal();
        cartRepository.save(cart);
    }

    @Override
    public CartItem addItemToCart(Long userId, Long bookId) throws BookNotFoundException, InvalidBookDataException {
        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));

        Book book = bookRepository.findById(bookId)
                                  .orElseThrow(() -> new BookNotFoundException("No se encontró el libro solicitado."));

        Optional<CartItem> existingCartItem = cart.getBooks()
                                                  .stream()
                                                  .filter(item -> item.getBook().getId().equals(bookId))
                                                  .findFirst();

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            if (book.getStock() <= 0) {
                throw new InvalidBookDataException("El libro no tiene suficiente stock.");
            }
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            if (book.getStock() <= 0) {
                throw new InvalidBookDataException("El libro no tiene suficiente stock.");
            }
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setQuantity(1);
            cart.getBooks().add(cartItem);
        }

        bookRepository.save(book);

        cartItem.updatePrice();
        cart.updateTotal();
        cartRepository.save(cart);

        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(CartItem cartItem) throws CartItemNotFoundException {
        CartItem existingCartItem = cartItemRepository.findById(cartItem.getId())
            .orElseThrow(() -> new CartItemNotFoundException("El artículo del carrito no se encuentra disponible."));

        existingCartItem.setQuantity(cartItem.getQuantity());
        existingCartItem.setPrice(existingCartItem.getBook().getPrice() * cartItem.getQuantity());

        cartItemRepository.save(existingCartItem);

        Cart cart = existingCartItem.getCart();
        cart.updateTotal();
        cartRepository.save(cart);

        return existingCartItem;
    }

    @Override
    public void deleteCartItem(Long id) throws CartItemNotFoundException {
        CartItem cartItem = cartItemRepository.findById(id)
            .orElseThrow(() -> new CartItemNotFoundException("El artículo del carrito no se encuentra disponible."));

        Cart cart = cartItem.getCart();
        cart.getBooks().remove(cartItem);
        cart.updateTotal();

        cartItemRepository.delete(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void checkoutCart(Long userId) throws CartNotFoundException {
        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));

        for (CartItem cartItem : cart.getBooks()) {
            Book book = cartItem.getBook();
            if (book.getStock() < cartItem.getQuantity()) {
                throw new InvalidBookDataException("El libro no tiene suficiente stock.");
            }
            book.setStock(book.getStock() - cartItem.getQuantity());
            bookRepository.save(book);
        }

        clearCart(userId);

        // TODO: Crear orden de compra

        cartRepository.save(cart);
    }


}
