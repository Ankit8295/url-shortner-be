package com.urlshort.redis;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

	private final StringRedisTemplate redisTemplate;

	public RedisService(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public Optional<String> get(String key) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(key));
	}

	public void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public void setWithExpiry(String key, String value, Duration ttl) {
		redisTemplate.opsForValue().set(key, value, ttl);
	}

	public boolean delete(String key) {
		return Boolean.TRUE.equals(redisTemplate.delete(key));
	}

	public boolean hasKey(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}
}
