package com.bookstore.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.backend.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
    //TODO agregar metodo para limpiar carrito
    //seleccionar todos los items del carrito
}
