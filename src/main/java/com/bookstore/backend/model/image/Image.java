package com.bookstore.backend.model.image;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Blob;
import java.util.Date;

import com.bookstore.backend.model.book.Book;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image_table")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Blob image;

    private Date date = new Date();

    // Relación con Book
    @OneToOne(mappedBy = "image")
    private Book book; // Relación inversa opcional
}
