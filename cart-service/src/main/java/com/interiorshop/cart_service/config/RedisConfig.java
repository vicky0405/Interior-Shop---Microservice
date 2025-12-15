package com.interiorshop.cart_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interiorshop.cart_service.model.CartItem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, CartItem> redisTemplate(
        RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, CartItem> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        ObjectMapper objectMapper = new ObjectMapper();
        // Không cần activateDefaultTyping nếu dùng cách này (vì đã chỉ định class cụ thể)
        // Nhưng nếu muốn hỗ trợ ngày tháng thì vẫn cần JavaTimeModule
        // objectMapper.registerModule(new JavaTimeModule());

        // CHÌA KHÓA Ở ĐÂY: Chỉ định rõ CartItem.class vào Serializer
        Jackson2JsonRedisSerializer<CartItem> serializer =
            new Jackson2JsonRedisSerializer<>(objectMapper, CartItem.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer); // Quan trọng cho opsForHash

        return template;
    }
}
