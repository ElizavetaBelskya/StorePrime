package ru.tinkoff.storePrime.mongo.model;


import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "photos")
@Setter
@Getter
public class Photo {
    @Id
    private String id;

    private String title;

    private Binary image;

}