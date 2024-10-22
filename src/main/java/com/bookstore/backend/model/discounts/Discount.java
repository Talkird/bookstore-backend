package com.bookstore.backend.model.discounts;

import lombok.Data;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private double percentage;

    @Column
    private Boolean isActive;

    @Column
    private LocalDateTime expirationDate;

    public boolean isValid() {
        return isActive && expirationDate.isAfter(LocalDateTime.now());
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
