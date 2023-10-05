package com.devlop.siren.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void setValue(String key, String value){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value);
    }
    public void setValue(String key, String value, Duration duration){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value, duration);
    }
    public String getValues(String key){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }
    public void deleteValue(String key){
        redisTemplate.delete(key);
    }
}
