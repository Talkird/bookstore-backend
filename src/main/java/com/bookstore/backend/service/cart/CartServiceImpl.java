package com.bookstore.backend.service.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.exception.book.InvalidBookDataException;
import com.bookstore.backend.exception.cart.CartItemNotFoundException;
import com.bookstore.backend.exception.cart.CartNotFoundException;
import com.bookstore.backend.model.book.Book;
import com.bookstore.backend.model.cart.Cart;
import com.bookstore.backend.model.cart.CartItem;
import com.bookstore.backend.model.order.Order;
import com.bookstore.backend.model.order.PaymentMethod;
import com.bookstore.backend.model.user.User;
import com.bookstore.backend.repository.CartItemRepository;
import com.bookstore.backend.repository.CartRepository;
import com.bookstore.backend.service.book.BookService;
import com.bookstore.backend.service.order.OrderService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private OrderService orderService;

    @Override
    public List<CartItem> getCart(Long userId) throws CartNotFoundException {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));
        return cart.getBooks();
    }

    @Override
    public void clearCart(Long userId) throws CartNotFoundException {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));
        cart.getBooks().clear();
        cart.updateTotal();
        cartRepository.save(cart);
    }

    @Override
    public CartItem addItemToCart(Long userId, Long bookId, int quantity) throws BookNotFoundException, InvalidBookDataException {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));

        Book book = bookService.getBookById(bookId);

        if (book.getStock() < quantity) {
            throw new InvalidBookDataException("El libro no tiene suficiente stock.");
        }

        Optional<CartItem> existingCartItem = cart.getBooks()
                .stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst();

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setQuantity(1);
            cart.getBooks().add(cartItem);
        }

        bookService.updateBook(book);

        cartItem.updatePrice();
        cart.updateTotal();
        cartRepository.save(cart);

        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(CartItem cartItem) throws CartItemNotFoundException {
        CartItem existingCartItem = cartItemRepository.findById(cartItem.getId())
                .orElseThrow(
                        () -> new CartItemNotFoundException("El artículo del carrito no se encuentra disponible."));

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
                .orElseThrow(
                        () -> new CartItemNotFoundException("El artículo del carrito no se encuentra disponible."));

        Cart cart = cartItem.getCart();
        cart.getBooks().remove(cartItem);
        cart.updateTotal();

        cartItemRepository.delete(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void checkoutCart(Long userId, String customerName, String customerEmail, 
    String customerPhone, String shippingAdress, PaymentMethod paymentMethod) throws CartNotFoundException {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));

        for (CartItem cartItem : cart.getBooks()) {
            Book book = cartItem.getBook();
            if (book.getStock() < cartItem.getQuantity()) {
                throw new InvalidBookDataException("El libro no tiene suficiente stock.");
            }
            book.setStock(book.getStock() - cartItem.getQuantity());
            bookService.updateBook(book);
        }

        clearCart(userId);

        Order order = new Order();
        order.setCart(cart);
        order.setUser(cart.getUser());
        order.setTotal(cart.getTotal());
        order.setCustomerName(customerName);
        order.setCustomerEmail(customerEmail);
        order.setCustomerPhone(customerPhone);
        order.setShippingAddress(shippingAdress);
        order.setPaymentMethod(paymentMethod);
        orderService.createOrder(order);


        cartRepository.save(cart);
    }
    
    @Override
    public void createCart(User user) throws UserNotFoundException {
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

}
