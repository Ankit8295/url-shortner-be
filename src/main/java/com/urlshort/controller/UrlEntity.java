package com.urlshort.controller;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "urls")
public class UrlEntity {

    @Id
    private UUID id;

    @Column(name = "long_url", nullable = false, unique = true, length = 5000)
    private String longUrl;

    @Column(name = "short_code", nullable = false, unique = true, length = 8)
    private String shortCode;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    protected UrlEntity() {
    }

    public UrlEntity(String longUrl, String shortCode) {
        this.id = UUID.randomUUID();
        this.longUrl = longUrl;
        this.shortCode = shortCode;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
