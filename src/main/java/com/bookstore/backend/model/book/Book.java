package com.bookstore.backend.model.book;

import java.util.List;

import com.bookstore.backend.model.rating.Rating;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column
    private Long isbn;

    @Column
    private int year;

    @Positive(message = "Price must be positive")
    @Column(nullable = false)
    private double price;

    @PositiveOrZero(message = "Stock must be zero or positive")
    @Column(nullable = false)
    private int stock;

    /*
     * @Lob
     * 
     * @Column(nullable = false)
     * private byte[] picture;
     */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "book")
    private List<Rating> ratings;

    @Column
    private double rating;

    public void updateAverageRating() {
        if (ratings == null || ratings.isEmpty())
            this.rating = 0.0;

        int total = 0;
        for (Rating r : ratings) {
            total += r.getRating();
        }

        this.rating = (double) total / ratings.size();
    }
}
