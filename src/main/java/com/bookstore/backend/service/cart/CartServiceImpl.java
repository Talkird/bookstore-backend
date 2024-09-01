package com.bookstore.backend.service.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.exception.book.InvalidBookDataException;
import com.bookstore.backend.exception.cart.CartItemNotFoundException;
import com.bookstore.backend.exception.cart.CartNotFoundException;
import com.bookstore.backend.exception.cart.InvalidCouponException;
import com.bookstore.backend.model.Book;
import com.bookstore.backend.model.Cart;
import com.bookstore.backend.model.CartItem;
import com.bookstore.backend.model.Discount;
import com.bookstore.backend.model.Order;
import com.bookstore.backend.repository.CartItemRepository;
import com.bookstore.backend.repository.CartRepository;
import com.bookstore.backend.repository.DiscountRepository;
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

    @Autowired
    private DiscountRepository discountRepository;

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

        Book book = bookService.getBookById(bookId);

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

        bookService.updateBook(book);

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
            bookService.updateBook(book);
        }

        clearCart(userId);

        /* 
        Order order = new Order();
        order.setCart(cart);
        order.setUser(cart.getUser());
        order.setTotal(cart.getTotal());
        orderService.createOrder(order);
        */
        //TODO agregar parametros para rellenar order

        cartRepository.save(cart);
    }

    @Override
    public void applyCouponToCart(Long userId, String couponCode) throws CartNotFoundException, InvalidCouponException {
        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));

        Discount discount = discountRepository.findByCodeAndActiveTrue(couponCode)
                                             .orElseThrow(() -> new InvalidCouponException("El cupón no es válido o no está activo."));

        if (discount.isExpired()) {
            throw new InvalidCouponException("El cupón ha expirado.");
        }

        // Asignar el descuento al carrito
        cart.setDiscount(discount);
        cart.updateTotal();
        cartRepository.save(cart);
    }

    @Override
    public void updateCartTotal(Long userId) throws CartNotFoundException {
        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));

        cart.updateTotal();
        cartRepository.save(cart);
    }

}
