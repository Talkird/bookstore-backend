package com.bookstore.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.bookstore.backend.model.Genre;

public interface GenreService {
    Page<Genre> getGenres(PageRequest pageRequest); //obtener una lista paginada de géneros
    
    Genre getGenreById(Long id); //obtener un género por su ID
    
    Genre createGenre(Genre genre); //crear un nuevo género
    
    Genre updateGenre(Long id, Genre genreDetails); //actualizar un género existente
    
    void deleteGenre(Long id); //eliminar un género por su ID
}

