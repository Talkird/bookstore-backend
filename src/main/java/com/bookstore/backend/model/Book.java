package com.bookstore.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Book{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column
    private String title;
    
    @Column
    private String author;
    
    @Column
    private String genre;
   
    @Column
    private int year;
   
    @Column
    private double price;

}
