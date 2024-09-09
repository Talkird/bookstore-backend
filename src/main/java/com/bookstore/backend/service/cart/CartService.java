package com.bookstore.backend.service.cart;

import java.util.List;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.exception.book.InvalidBookDataException;
import com.bookstore.backend.exception.cart.CartItemNotFoundException;
import com.bookstore.backend.exception.cart.CartNotFoundException;
import com.bookstore.backend.model.cart.CartItem;
import com.bookstore.backend.model.dto.CartItemRequest;
import com.bookstore.backend.model.order.PaymentMethod;
import com.bookstore.backend.model.user.User;

public interface CartService {

    public void createCart(User user) throws UserNotFoundException;

    public List<CartItemRequest> getCart(Long userId) throws CartNotFoundException;
    
    public void clearCart(Long userId) throws UserNotFoundException, CartNotFoundException;

    public CartItem addItemToCart(Long userId, Long bookId, int quantity) throws UserNotFoundException, BookNotFoundException, InvalidBookDataException;

    public CartItem updateCartItem(Long userId,Long id,Long bookId,int quantity) throws CartItemNotFoundException,InvalidBookDataException;

    public void deleteCartItem(Long id, Long userId) throws CartItemNotFoundException;

    public void checkoutCart(Long userId, String customerName, String customerEmail, 
    String customerPhone, String shippingAddress, PaymentMethod paymentMethod, String discountCode) throws UserNotFoundException, CartNotFoundException;

}