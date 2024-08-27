package com.bookstore.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.bookstore.backend.model.Genre;
import com.bookstore.backend.repository.GenreRepository;

import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public Page<Genre> getGenres(PageRequest pageRequest) {
        return genreRepository.findAll(pageRequest);
    }

    @Override
    public Genre getGenreById(Long id) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);
        if (optionalGenre.isPresent()) {
            return optionalGenre.get();
        } else {
            // Manejo de excepción o retorno de null si no se encuentra el género
            throw new RuntimeException("Genre not found for id: " + id);
        }
    }

    @Override
    public Genre createGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public Genre updateGenre(Long id, Genre genreDetails) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);
        if (optionalGenre.isPresent()) {
            Genre genre = optionalGenre.get();
            genre.setName(genreDetails.getName());
            genre.setDescription(genreDetails.getDescription());
            return genreRepository.save(genre);
        } else {
            // Manejo de excepción o retorno de null si no se encuentra el género
            throw new RuntimeException("Genre not found for id: " + id);
        }
    }

    @Override
    public void deleteGenre(Long id) {
        if (genreRepository.existsById(id)) {
            genreRepository.deleteById(id);
        } else {
            // Manejo de excepción si no se encuentra el género para eliminar
            throw new RuntimeException("Genre not found for id: " + id);
        }
    }
}
