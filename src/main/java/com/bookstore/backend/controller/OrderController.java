package com.bookstore.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.backend.service.order.OrderService;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    

}
