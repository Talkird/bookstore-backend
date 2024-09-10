package com.bookstore.backend.service.cart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.backend.exception.InvalidDiscountException;
import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.exception.book.InvalidBookDataException;
import com.bookstore.backend.exception.cart.CartItemNotFoundException;
import com.bookstore.backend.exception.cart.CartNotFoundException;
import com.bookstore.backend.model.book.Book;
import com.bookstore.backend.model.cart.Cart;
import com.bookstore.backend.model.cart.CartItem;
import com.bookstore.backend.model.dto.BookRequest;
import com.bookstore.backend.model.dto.BookResponse;
import com.bookstore.backend.model.dto.CartItemRequest;
import com.bookstore.backend.model.dto.CartItemResponse;
import com.bookstore.backend.model.dto.OrderRequest;
import com.bookstore.backend.model.order.Order;
import com.bookstore.backend.model.order.OrderStatus;
import com.bookstore.backend.model.order.PaymentMethod;
import com.bookstore.backend.model.user.User;
import com.bookstore.backend.repository.CartItemRepository;
import com.bookstore.backend.repository.CartRepository;
import com.bookstore.backend.service.book.BookService;
import com.bookstore.backend.service.discount.DiscountService;
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
    private DiscountService discountService;

    @Override
    public List<CartItemResponse> getCart(Long userId) throws CartNotFoundException {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));

        return CartItemResponse.convertToCartItemResponse(cart.getBooks());
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
    public CartItemResponse addItemToCart(Long userId, CartItemRequest cartItemRequest)
            throws BookNotFoundException, InvalidBookDataException {
        int quantity = cartItemRequest.getQuantity();
        Long bookId = cartItemRequest.getBookId(); // cambiado
        if (quantity <= 0) {
            throw new InvalidBookDataException("La cantidad debe ser mayor a 0.");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));

        Book book = BookResponse.convertToBook(bookService.getBookById(bookId));

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
            cartItem.setQuantity(quantity);
            cart.getBooks().add(cartItem);
        }

        cartItem.updatePrice();
        cart.updateTotal();
        // cartRepository.save(cart); removed

        return CartItemResponse.convertToCartItemResponse(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemResponse updateCartItem(Long userId, Long cartItemId, CartItemRequest cartItemRequest)
            throws CartItemNotFoundException, InvalidBookDataException {
        int quantity = cartItemRequest.getQuantity();
        Long bookId = cartItemRequest.getBookId(); // cambiado
        Long id = cartItemId; // cambiado
        if (quantity <= 0) {
            throw new InvalidBookDataException("La cantidad debe ser mayor a 0.");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));

        Book book = BookResponse.convertToBook(bookService.getBookById(bookId));

        if (book.getStock() < quantity) {
            throw new InvalidBookDataException("El libro no tiene suficiente stock.");
        }

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(
                        () -> new CartItemNotFoundException("El artículo del carrito no se encuentra disponible."));

        cartItem.setQuantity(quantity);
        cartItem.updatePrice();
        cart.updateTotal();
        cartRepository.save(cart);

        return CartItemResponse.convertToCartItemResponse(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) throws CartItemNotFoundException {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(
                        () -> new CartItemNotFoundException("El artículo del carrito no se encuentra disponible."));

        Cart cart = cartItem.getCart();

        cart.getBooks().remove(cartItem);
        cart.updateTotal();
        cartItemRepository.delete(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void checkoutCart(Long userId, OrderRequest orderRequest)
            throws UserNotFoundException, CartNotFoundException {
        String discountCode = orderRequest.getDiscountCode();
        PaymentMethod paymentMethod = orderRequest.getPaymentMethod();
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));

        double totalPrice = cart.getTotal();

        // Aplicar descuento por cantidad de productos (más de 5 productos)
        int totalItems = cart.getBooks().stream().mapToInt(CartItem::getQuantity).sum();
        if (totalItems > 5) {
            totalPrice = totalPrice * 0.9; // 10% de descuento si hay más de 5 productos
        }

        // Aplicar descuento por código de descuento
        if (discountCode != null && !discountCode.isEmpty()) {
            try {
                totalPrice = discountService.applyDiscount(discountCode, totalPrice);
            } catch (InvalidDiscountException e) {
                System.out.println(e.getMessage());
            }
        }

        // Aplicar descuento por método de pago (ejemplo: tarjeta de crédito tiene 5% de
        // descuento)
        if (paymentMethod == PaymentMethod.CREDIT_CARD) {
            totalPrice = totalPrice * 0.95; // 5% de descuento
        }

        // Verificar stock y actualizar inventario
        for (CartItem cartItem : cart.getBooks()) {
            Book book = cartItem.getBook();
            if (book.getStock() < cartItem.getQuantity()) {
                throw new InvalidBookDataException("El libro" + book.getTitle() + "no tiene suficiente stock.");
            }
            book.setStock(book.getStock() - cartItem.getQuantity());
            BookRequest bookRequest = BookRequest.convertToBookRequest(book);
            bookService.updateBook(book.getId(), bookRequest);
        }

        // Crear el pedido
        Order order = new Order();
        order.setCart(cart);
        order.setUser(cart.getUser());
        order.setTotal(totalPrice); // Se usa el total con descuentos aplicados
        order.setCustomerName(orderRequest.getCustomerName());
        order.setCustomerEmail(orderRequest.getCustomerEmail());
        order.setCustomerPhone(orderRequest.getCustomerPhone());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setPaymentMethod(paymentMethod);
        order.setDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        orderService.createOrder(order);

        // Limpiar el carrito
        clearCart(userId);
        cartRepository.save(cart);
    }

    @Override
    public void createCart(User user) throws UserNotFoundException {
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

}
