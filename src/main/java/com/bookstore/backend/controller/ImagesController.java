package com.bookstore.backend.controller;


import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.backend.model.dto.AddFileRequest;
import com.bookstore.backend.model.image.Image;
import com.bookstore.backend.service.image.ImageService;

@RestController
@RequestMapping("/images")
public class ImagesController {
    @Autowired
    private ImageService imageService;

    @CrossOrigin
    /*@GetMapping("/{id}")
    public ResponseEntity<ImageResponse> displayImage(@PathVariable("id") long id) throws IOException, SQLException {
        Image image = imageService.viewById(id);
        String encodedString = Base64.getEncoder()
                .encodeToString(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok().body(ImageResponse.builder().file(encodedString).id(id).build());
    }
    */
    @PostMapping("/add")
    public String addImagePost(AddFileRequest request) throws IOException, SerialException, SQLException {
        byte[] bytes = request.getFile().getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        imageService.create(Image.builder().image(blob).build());
        return "created";
    }
}
