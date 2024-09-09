package com.bookstore.backend.service.image;

import org.springframework.stereotype.Service;

import com.bookstore.backend.model.image.Image;

@Service
public interface ImageService {
    public Image create(Image image);

    public Image viewById(long id);
}
