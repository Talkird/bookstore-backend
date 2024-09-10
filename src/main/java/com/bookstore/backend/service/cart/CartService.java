package com.bookstore.backend.service.cart;

import java.util.List;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.exception.book.InvalidBookDataException;
import com.bookstore.backend.exception.cart.CartItemNotFoundException;
import com.bookstore.backend.exception.cart.CartNotFoundException;
import com.bookstore.backend.model.dto.CartItemRequest;
import com.bookstore.backend.model.dto.CartItemResponse;
import com.bookstore.backend.model.dto.OrderRequest;
import com.bookstore.backend.model.dto.OrderResponse;
import com.bookstore.backend.model.user.User;

public interface CartService {

        public void createCart(User user) throws UserNotFoundException;

        public List<CartItemResponse> getCart(Long userId) throws CartNotFoundException;

        public void clearCart(Long userId) throws UserNotFoundException, CartNotFoundException;

        public CartItemResponse addItemToCart(Long userId, CartItemRequest cartItemRequest)
                        throws UserNotFoundException, BookNotFoundException, InvalidBookDataException;

        public CartItemResponse updateCartItem(Long userId, Long cartItemId, CartItemRequest cartItemRequest)
                        throws CartItemNotFoundException, InvalidBookDataException;

        public void deleteCartItem(Long userId, Long cartItemId) throws CartItemNotFoundException;

        public OrderResponse checkoutCart(Long userId, OrderRequest orderRequest)
                        throws UserNotFoundException, CartNotFoundException;

}