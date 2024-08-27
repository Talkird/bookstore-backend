package com.bookstore.backend.controller;


import com.bookstore.backend.model.Genre;
import com.bookstore.backend.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping
    public Page<Genre> getAllGenres(
        @RequestParam(defaultValue = "0") int page, // Parámetro de página con valor por defecto
        @RequestParam(defaultValue = "10") int size // Parámetro de tamaño de página con valor por defecto
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return genreService.getGenres(pageRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable Long id) {
        try {
            Genre genre = genreService.getGenreById(id);
            return ResponseEntity.ok(genre);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Genre createGenre(@RequestBody Genre genre) {
        return genreService.createGenre(genre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @RequestBody Genre genreDetails) {
        Genre updatedGenre = genreService.updateGenre(id, genreDetails);
        if (updatedGenre != null) {
            return ResponseEntity.ok(updatedGenre);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}

