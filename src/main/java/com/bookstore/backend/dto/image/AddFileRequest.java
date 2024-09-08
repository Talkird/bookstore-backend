package com.bookstore.backend.dto.image;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddFileRequest {
    private String name;
    private MultipartFile file;
}