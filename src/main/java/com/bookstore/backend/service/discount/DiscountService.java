package com.bookstore.backend.service.discount;

import java.util.List;

import com.bookstore.backend.exception.InvalidDiscountException;
import com.bookstore.backend.model.discounts.Discount;


public interface DiscountService{
    
    public double applyDiscount(String discountCode, double totalPrice) throws InvalidDiscountException;

    public Discount createDiscount(Discount discount);

    public List<Discount> getAllActiveDiscounts();

    public Discount updateDiscount(Long id, Discount newDiscountData) throws InvalidDiscountException;

    public void deleteDiscount(Long id) throws InvalidDiscountException;


}