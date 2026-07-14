package com.urlshort.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/url")
public class UrlShortController {

    private final UrlService urlService;

    public UrlShortController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/short")
    public ResponseEntity<String> createShortUrl(@RequestBody ShortenUrlRequest request) {
        try {
            String shortUrl = urlService.generateShortUrl(request.longUrl());
            return ResponseEntity.ok(shortUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sho.rt/{shortCode}")
    public ResponseEntity<Void> redirectShortCodeToLongUrl(@PathVariable String shortCode) {
        try {
            String longUrl = urlService.getLongUrl(shortCode);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(longUrl))
                    .build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
