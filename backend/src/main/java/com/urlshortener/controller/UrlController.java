package com.urlshortener.controller;

import com.urlshortener.dto.UrlRequest;
import com.urlshortener.dto.UrlResponse;
import com.urlshortener.entity.Payment;
import com.urlshortener.entity.User;
import com.urlshortener.entity.Url;
import com.urlshortener.service.AnalyticsService;
import com.urlshortener.service.AuthService;
import com.urlshortener.service.PaymentService;
import com.urlshortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Tag(name = "URL Shortener", description = "URL shortening and redirection APIs")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AnalyticsService analyticsService;

    @PostMapping("/api/url/shorten")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Create short URL after payment confirmation")
    public ResponseEntity<UrlResponse> shortenUrl(
            @Valid @RequestBody UrlRequest request,
            Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());

        // Verify payment via reference ID
        Payment payment = paymentService.getPaymentByReference(request.getPaymentReferenceId());

        if (!payment.getUserId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!"CONFIRMED".equals(payment.getStatus())) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).build();
        }

        // Create short URL
        UrlResponse response = urlService.createShortUrl(request.getOriginalUrl(), user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/url/user")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Get all URLs for logged-in user")
    public ResponseEntity<List<UrlResponse>> getUserUrls(Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        List<UrlResponse> urls = urlService.getUserUrls(user.getId());
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/{shortCode}")
    @Operation(summary = "Redirect to original URL")
    public void redirectUrl(
            @PathVariable("shortCode") String shortCode,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String originalUrl = urlService.getOriginalUrl(shortCode);

        // Track analytics
        Url url = urlService.getUrlByShortCode(shortCode);
        String ipAddress = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String referrer = request.getHeader("Referer");

        analyticsService.trackClick(url.getId(), ipAddress, userAgent, referrer);

        // Redirect
        response.sendRedirect(originalUrl);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
