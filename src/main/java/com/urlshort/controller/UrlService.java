package com.urlshort.controller;

import java.net.URI;
import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.urlshort.redis.RedisService;

import jakarta.transaction.Transactional;

@Service
public class UrlService {

    private static final String REDIS_KEY_PREFIX = "url:";
    private static final int SHORT_CODE_LENGTH = 8;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final SecureRandom secureRandom = new SecureRandom();

    private final UrlRepository urlRepository;
    private final RedisService redisService;
    private final String baseUrl;

    public UrlService(
            UrlRepository urlRepository,
            RedisService redisService,
            @Value("${app.base-url}") String baseUrl) {
        this.urlRepository = urlRepository;
        this.redisService = redisService;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }

    @Transactional
    public String getLongUrl(String shortCode) {
        String cacheKey = REDIS_KEY_PREFIX + shortCode;

        Optional<String> cachedUrl = redisService.get(cacheKey);
        if (cachedUrl.isPresent()) {
            return cachedUrl.get();
        }

        UrlEntity url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

        redisService.set(cacheKey, url.getLongUrl());
        return url.getLongUrl();
    }

    @Transactional
    public String generateShortUrl(String longUrl) {
        validateUrl(longUrl);

        Optional<UrlEntity> existing = urlRepository.findByLongUrl(longUrl);

        if (existing.isPresent()) {
            return baseUrl + existing.get().getShortCode();
        }

        String shortCode = generateUniqueShortCode();
        UrlEntity url = new UrlEntity(longUrl, shortCode);
        urlRepository.save(url);

        return baseUrl + shortCode;
    }

    public void validateUrl(String longUrl) {
        try {
            new URI(longUrl).toURL();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL");
        }
    }

    private String randomCode() {
        StringBuilder stringBuilder = new StringBuilder(SHORT_CODE_LENGTH);

        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            stringBuilder.append(CHARACTERS.charAt(secureRandom.nextInt(CHARACTERS.length())));
        }
        return stringBuilder.toString();
    }

    private String generateUniqueShortCode() {
        String shortCode;

        do {
            shortCode = randomCode();
        } while (urlRepository.existsByShortCode(shortCode));

        return shortCode;
    }
}
