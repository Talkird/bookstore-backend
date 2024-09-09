package com.bookstore.backend.service.cart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.bookstore.backend.model.dto.CartItemRequest;
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
    public List<CartItemRequest> getCart(Long userId) throws CartNotFoundException {
    Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));
    
    // Convertir cada CartItem en un CartItemResponse
    return cart.getBooks().stream().map((CartItem cartItem) -> 
        new CartItemRequest(
            cartItem.getBook().getId(),
            cartItem.getBook().getTitle(),
            cartItem.getBook().getAuthor(),
            cartItem.getQuantity(),
            cartItem.getBook().getPrice() * cartItem.getQuantity()
        )
    ).collect(Collectors.toList());
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
        if (quantity <= 0) {
            throw new InvalidBookDataException("La cantidad debe ser mayor a 0.");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));

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
            cartItem.setQuantity(quantity); // Aquí se usa la cantidad solicitada
            cart.getBooks().add(cartItem);
        }

        // Actualiza el stock del libro
        book.setStock(book.getStock() - quantity);
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
    public void deleteCartItem(Long id, Long userId) throws CartItemNotFoundException{
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException("El artículo del carrito no se encuentra disponible."));

        Cart cart = cartItem.getCart();

        cart.getBooks().remove(cartItem);
        cart.updateTotal();

        int quantity = cartItem.getQuantity();
        Book book = cartItem.getBook();
        

        book.setStock(book.getStock() + quantity);
        bookService.updateBook(book);

        cartItemRepository.delete(cartItem);
        cartRepository.save(cart);
    }

    

    @Override
    public void checkoutCart(Long userId, String customerName, String customerEmail, String customerPhone,
            String shippingAddress, PaymentMethod paymentMethod, String discountCode) throws UserNotFoundException, CartNotFoundException {
                Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("El carrito no fue encontrado para el usuario especificado."));

                System.out.println("Nombre del cliente: " + customerEmail);
                System.out.println("Email del cliente: " + customerName);
                System.out.println("Teléfono del cliente: " + customerPhone);
                System.out.println("Dirección de envío: " + shippingAddress);


        double totalPrice = cart.getTotal();
        System.out.println("Dirección de envío: " + totalPrice);

        // Aplicar descuento por cantidad de productos (más de 5 productos)
        long totalItems = cart.getBooks().stream().mapToInt(CartItem::getQuantity).sum();
        if (totalItems > 5) {
            totalPrice = totalPrice * 0.9;  // 10% de descuento si hay más de 5 productos
        }
        System.out.println("Precio con descuento por cantidad: " + totalPrice);

        //Aplicar descuento por código de descuento
        if (discountCode != null && !discountCode.isEmpty()) {
            try {
                totalPrice = discountService.applyDiscount(discountCode, totalPrice);
            } catch (InvalidDiscountException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Precio con descuento por código: " + totalPrice);

        // Aplicar descuento por método de pago (ejemplo: tarjeta de crédito tiene 5% de descuento)
        if (paymentMethod == PaymentMethod.CREDIT_CARD) {
            totalPrice = totalPrice * 0.95;  // 5% de descuento
        }
        System.out.println("Precio con descuento por medio de pago: " + totalPrice);

        // Verificar stock y actualizar inventario
        /*for (CartItem cartItem : cart.getBooks()) {
            Book book = cartItem.getBook();
            if (book.getStock() < cartItem.getQuantity()) {
                throw new InvalidBookDataException("El libro no tiene suficiente stock.");
            }
            book.setStock(book.getStock() - cartItem.getQuantity());
            bookService.updateBook(book);
        }*/

        // Crear el pedido
        Order order = new Order();
        order.setCart(cart);
        order.setUser(cart.getUser());
        order.setTotal(totalPrice);  // Se usa el total con descuentos aplicados
        order.setCustomerName(customerName);
        order.setCustomerEmail(customerEmail);
        order.setCustomerPhone(customerPhone);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        orderService.createOrder(order);

        // Limpiar el carrito
        clearCart(userId);
        cartRepository.save(cart);;
    }
    
    @Override
    public void createCart(User user) throws UserNotFoundException {
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }


}
