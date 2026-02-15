package com.urlshortener.service;

import com.urlshortener.dto.UrlResponse;
import com.urlshortener.entity.Url;
import com.urlshortener.exception.ResourceNotFoundException;
import com.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UrlService {

    @Value("${app.url.base-url}")
    private String baseUrl;

    @Value("${app.url.short-code-length}")
    private Integer shortCodeLength;

    @Autowired
    private UrlRepository urlRepository;

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public UrlResponse createShortUrl(String originalUrl, Long userId) {
        // Generate unique short code
        String shortCode = generateUniqueShortCode();

        // Save URL
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);
        url.setUserId(userId);
        url.setTotalClicks(0);
        url.setActive(true);

        Url savedUrl = urlRepository.save(url);

        // Return response
        return mapToResponse(savedUrl);
    }

    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = generateRandomCode();
        } while (urlRepository.existsByShortCode(shortCode));
        return shortCode;
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(shortCodeLength);
        for (int i = 0; i < shortCodeLength; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    public String getOriginalUrl(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

        if (!url.isActive()) {
            throw new ResourceNotFoundException("This URL has been deactivated");
        }

        return url.getOriginalUrl();
    }

    public List<UrlResponse> getUserUrls(Long userId) {
        List<Url> urls = urlRepository.findByUserId(userId);
        return urls.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void incrementClickCount(Long urlId) {
        Url url = urlRepository.findById(urlId)
                .orElseThrow(() -> new ResourceNotFoundException("URL not found"));
        url.setTotalClicks(url.getTotalClicks() + 1);
        urlRepository.save(url);
    }

    private UrlResponse mapToResponse(Url url) {
        UrlResponse response = new UrlResponse();
        response.setId(url.getId());
        response.setOriginalUrl(url.getOriginalUrl());
        response.setShortCode(url.getShortCode());
        response.setShortUrl(baseUrl + "/" + url.getShortCode());
        response.setTotalClicks(url.getTotalClicks());
        response.setCreatedAt(url.getCreatedAt());
        return response;
    }

    public Url getUrlByShortCode(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));
    }
}
