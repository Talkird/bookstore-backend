package com.bookstore.backend.service.image;

import com.bookstore.backend.model.image.Image;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {
    public Image create(Image image);

    public Image viewById(long id);

    // Nuevo método para obtener todas las imágenes
    List<Image> getAllImages();
}
