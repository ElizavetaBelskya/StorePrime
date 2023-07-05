package ru.tinkoff.storePrime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tinkoff.storePrime.mongo.model.Photo;
import ru.tinkoff.storePrime.mongo.service.PhotoService;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/photos/add")
    public ResponseEntity<String> addPhoto(@RequestBody MultipartFile image) throws IOException {
        String id = photoService.addPhoto("new", image);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/photos/{id}")
    public ResponseEntity<String> getPhoto(@PathVariable String id) {
        Photo photo = photoService.getPhoto(id);
        return ResponseEntity.ok(Base64.getEncoder().encodeToString(photo.getImage().getData()));
    }

}
