package com.urlshort.controller;

import java.net.URI;
import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UrlService {

    private static final String BASE_URL = "http://localhost:8080/url/sho.rt/";
    private static final int SHORT_CODE_LENGTH = 8;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final SecureRandom secureRandom = new SecureRandom();

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Transactional
    public String getLongUrl(String shortCode) {
        UrlEntity url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));
        return url.getLongUrl();
    }

    @Transactional
    public String generateShortUrl(String longUrl) {
        validateUrl(longUrl);

        Optional<UrlEntity> existing = urlRepository.findByLongUrl(longUrl);

        if (existing.isPresent()) {
            return BASE_URL + existing.get().getShortCode();
        }

        String shortCode = generateUniqueShortCode();
        UrlEntity url = new UrlEntity(longUrl, shortCode);
        urlRepository.save(url);

        return BASE_URL + shortCode;
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
