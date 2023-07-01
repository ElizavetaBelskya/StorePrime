package ru.tinkoff.storePrime.security.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.storePrime.security.repositories.BlackListRepository;

@RequiredArgsConstructor
@Repository
public class BlackListRedisRepositoryImpl implements BlackListRepository {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String token) {
        redisTemplate.opsForValue().set(token, "");
    }

    @Override
    public boolean exists(String token) {
        Boolean hasToken = redisTemplate.hasKey(token);
        return hasToken != null && hasToken;
    }
}
