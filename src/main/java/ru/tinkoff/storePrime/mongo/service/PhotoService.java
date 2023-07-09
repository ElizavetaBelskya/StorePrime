package ru.tinkoff.storePrime.mongo.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.tinkoff.storePrime.exceptions.not_found.PhotoNotFoundException;
import ru.tinkoff.storePrime.mongo.model.Photo;
import ru.tinkoff.storePrime.mongo.repository.PhotoRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepo;

    public String addPhoto(String title, MultipartFile file) throws IOException {
        Photo photo = new Photo();
        photo.setTitle(title);
        photo.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        photo = photoRepo.insert(photo);
        photoRepo.save(photo);
        return photo.getId();
    }

    public Photo getPhoto(String id) {
        return photoRepo.findById(id).orElseThrow(() -> new PhotoNotFoundException("Фотография не найдена"));
    }


}
