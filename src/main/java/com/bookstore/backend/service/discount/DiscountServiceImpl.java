package com.bookstore.backend.service.discount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.backend.exception.InvalidDiscountException;
import com.bookstore.backend.model.discounts.Discount;
import com.bookstore.backend.repository.DiscountRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService{

    @Autowired
    private DiscountRepository discountRepository;

    public double applyDiscount(String discountCode, double totalPrice) throws InvalidDiscountException {
        Optional<Discount> discount = discountRepository.findByCode(discountCode);

        if (discount.isPresent() && discount.get().isValid()) {
            double discountAmount = totalPrice * (discount.get().getPercentage() / 100);
            System.out.println("El descuento puede realizarse");
            return totalPrice - discountAmount;
        } else {
            throw new InvalidDiscountException("Código de descuento no válido o expirado.");
        }
    }

    public Discount createDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    public List<Discount> getAllActiveDiscounts() {
        return discountRepository.findAll().stream()
                .filter(Discount::isValid)
                .collect(Collectors.toList());
    }

    public Discount updateDiscount(Long id, Discount newDiscountData) throws InvalidDiscountException {
        return discountRepository.findById(id)
            .map(discount -> {
                discount.setCode(newDiscountData.getCode());
                discount.setPercentage(newDiscountData.getPercentage());
                discount.setIsActive(newDiscountData.getIsActive());
                discount.setExpirationDate(newDiscountData.getExpirationDate());
                return discountRepository.save(discount);
            })
            .orElseThrow(() -> new InvalidDiscountException("Descuento no encontrado con id: " + id));
    }

    public void deleteDiscount(Long id) throws InvalidDiscountException {
        if (discountRepository.existsById(id)) {
            discountRepository.deleteById(id);
        } else {
            throw new InvalidDiscountException("Descuento no encontrado con id: " + id);
        }
    }

}
