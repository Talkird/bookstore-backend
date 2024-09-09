package com.bookstore.backend.model.book;

public enum Genre {
    NOVELA,
    ROMANTICO,
    TERROR,
    CIENCIA_FICCION,
    FANTASIA,
    AVENTURAS,
    SUSPENSO,
    POESIA,
    INFANTIL,
    AUTOAYUDA,
    DEPORTE,
    ARTE,
    MUSICA,
    COCINA;

    public static Genre fromString(String genre) {
        try {
            return Genre.valueOf(genre.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Género inválido: " + genre);
        }
    }
}
