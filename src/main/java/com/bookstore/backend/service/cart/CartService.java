package com.bookstore.backend.service.cart;

import java.util.List;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.exception.book.InvalidBookDataException;
import com.bookstore.backend.exception.cart.CartItemNotFoundException;
import com.bookstore.backend.exception.cart.CartNotFoundException;
import com.bookstore.backend.model.cart.CartItem;
import com.bookstore.backend.model.order.PaymentMethod;

public interface CartService {

    public List<CartItem> getCart(Long userId) throws CartNotFoundException;
    
    public void clearCart(Long userId) throws UserNotFoundException, CartNotFoundException;

    public CartItem addItemToCart(Long userId, Long bookId, int quantity) throws UserNotFoundException, BookNotFoundException, InvalidBookDataException;

    public CartItem updateCartItem(CartItem cartItem) throws CartItemNotFoundException;

    public void deleteCartItem(Long id) throws CartItemNotFoundException;

    public void checkoutCart(Long userId, String customerName, String customerEmail, 
    String customerPhone, String shippingAdress, PaymentMethod paymentMethod) throws UserNotFoundException, CartNotFoundException;

}