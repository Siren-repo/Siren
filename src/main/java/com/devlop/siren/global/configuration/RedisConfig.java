package com.devlop.siren.global.configuration;

import com.devlop.siren.domain.order.dto.request.OrderItemRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${spring.redis.first.host}")
  private String host;

  @Value("${spring.redis.first.port}")
  private int port;

  @Value("${spring.redis.second.host}")
  private String cart_host;

  @Value("${spring.redis.second.port}")
  private int cart_port;

  @Bean(name = "redisConnectionFactory")
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(host, port);
  }

  @Bean(name = "cartRedisConnectionFactory")
  public RedisConnectionFactory cartRedisConnectionFactory() {
    return new LettuceConnectionFactory(cart_host, cart_port);
  }

  @Bean(name = "redisTemplate")
  public RedisTemplate<String, String> redisTemplate() {
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    return redisTemplate;
  }

  @Bean(name = "cartRedisTemplate")
  public RedisTemplate<String, OrderItemRequest> cartRedisTemplate() {
    RedisTemplate<String, OrderItemRequest> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(cartRedisConnectionFactory());

    // Key Serializer 설정
    redisTemplate.setKeySerializer(new StringRedisSerializer());

    // Value Serializer 설정
    Jackson2JsonRedisSerializer<OrderItemRequest> jsonRedisSerializer =
        new Jackson2JsonRedisSerializer<>(OrderItemRequest.class);
    redisTemplate.setValueSerializer(jsonRedisSerializer);

    // Hash Key Serializer 설정
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());

    // Hash Value Serializer 설정
    redisTemplate.setHashValueSerializer(jsonRedisSerializer);

    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }
}
