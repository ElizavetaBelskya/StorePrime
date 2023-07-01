package ru.tinkoff.storePrime.security.repositories;

public interface BlackListRepository {
    void save(String token);
    boolean exists(String token);
}
