package com.bookstore.backend.service.image;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.backend.model.image.Image;
import com.bookstore.backend.repository.ImageRepository;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Override
    @Transactional  // Asegura que el método funcione dentro de una transacción
    public Image create(Image image) {
        return imageRepository.save(image);
    }

    @Transactional
    @Override
    public Image viewById(long id) {
        return imageRepository.findById(id).get();
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll(); // Obtiene todas las imágenes
    }
}
