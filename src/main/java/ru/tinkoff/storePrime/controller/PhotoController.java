package ru.tinkoff.storePrime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tinkoff.storePrime.controller.api.PhotoApi;
import ru.tinkoff.storePrime.dto.photo.PhotoResponse;
import ru.tinkoff.storePrime.mongo.model.Photo;
import ru.tinkoff.storePrime.mongo.service.PhotoService;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PhotoController implements PhotoApi {

    private final PhotoService photoService;

    public ResponseEntity<String> addPhoto(MultipartFile image) throws IOException {
        String id = photoService.addPhoto(UUID.randomUUID() + image.getName(), image);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    public ResponseEntity getPhoto(String id) {
        Photo photo = photoService.getPhoto(id);
        byte[] imageData = photo.getImage().getData();
        ByteArrayResource resource = new ByteArrayResource(imageData);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }



}
