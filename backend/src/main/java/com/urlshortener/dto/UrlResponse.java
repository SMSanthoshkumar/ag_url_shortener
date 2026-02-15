package com.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlResponse {
    private Long id;
    private String originalUrl;
    private String shortCode;
    private String shortUrl;
    private Integer totalClicks;
    private LocalDateTime createdAt;
}
