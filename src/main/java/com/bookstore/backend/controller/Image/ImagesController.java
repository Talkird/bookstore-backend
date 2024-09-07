package com.bookstore.backend.controller.Image;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bookstore.backend.model.image.Image;
import com.bookstore.backend.service.image.ImageService;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("images")
public class ImagesController {
    @Autowired
    private ImageService imageService;

    @CrossOrigin
    @GetMapping()
    public ResponseEntity<ImageResponse> displayImage(@RequestParam("id") long id) throws IOException, SQLException {
        Image image = imageService.viewById(id);
        String encodedString = Base64.getEncoder()
                .encodeToString(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok().body(ImageResponse.builder().file(encodedString).id(id).build());
    }

    @SuppressWarnings("null")
    @PostMapping()
    public String addImagePost(AddFileRequest request) throws IOException, SerialException, SQLException {
        MultipartFile file = request.getFile();

        // Validar que el archivo no sea nulo
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        // Validar que el archivo sea de tipo JPEG
        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg")) {
            throw new IllegalArgumentException("Solo se permiten archivos JPEG");
        }

        // Convertir el archivo a Blob y guardarlo
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        imageService.create(Image.builder().image(blob).build());

        return "created";
    }

    @GetMapping("/all")
    public ResponseEntity<List<Long>> getAllImageIds() {
        List<Image> images = imageService.getAllImages();
        List<Long> imageIds = images.stream()
                                    .map(Image::getId)  // Extrae el ID de cada imagen
                                    .collect(Collectors.toList()); // Recoge todos los IDs en una lista
        return ResponseEntity.ok(imageIds);
    }
}
