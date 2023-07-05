package ru.tinkoff.storePrime.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.tinkoff.storePrime.mongo.model.Photo;

public interface PhotoRepository extends MongoRepository<Photo, String> {

}
