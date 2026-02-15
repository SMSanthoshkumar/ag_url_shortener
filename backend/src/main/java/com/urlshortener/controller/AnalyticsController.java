package com.urlshortener.controller;

import com.urlshortener.dto.AnalyticsResponse;
import com.urlshortener.entity.User;
import com.urlshortener.service.AnalyticsService;
import com.urlshortener.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics", description = "URL analytics and statistics APIs")
@SecurityRequirement(name = "bearer-jwt")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private AuthService authService;

    @GetMapping("/{shortCode}")
    @Operation(summary = "Get analytics for a specific URL")
    public ResponseEntity<AnalyticsResponse> getUrlAnalytics(@PathVariable("shortCode") String shortCode) {
        AnalyticsResponse analytics = analyticsService.getUrlAnalyticsByShortCode(shortCode);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/user")
    @Operation(summary = "Get aggregate analytics for logged-in user")
    public ResponseEntity<Map<LocalDate, Long>> getUserAnalytics(Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        Map<LocalDate, Long> analytics = analyticsService.getUserAnalytics(user.getId());
        return ResponseEntity.ok(analytics);
    }
}
