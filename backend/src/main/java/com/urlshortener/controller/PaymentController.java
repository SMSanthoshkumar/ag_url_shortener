package com.urlshortener.controller;

import com.google.zxing.WriterException;
import com.urlshortener.dto.QrCodeResponse;
import com.urlshortener.entity.User;
import com.urlshortener.service.AuthService;
import com.urlshortener.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@Tag(name = "Payment", description = "QR Code payment APIs")
@SecurityRequirement(name = "bearer-jwt")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AuthService authService;

    @PostMapping("/generate-qr")
    @Operation(summary = "Generate QR code for payment")
    public ResponseEntity<QrCodeResponse> generateQrCode(Authentication authentication)
            throws WriterException, IOException {
        User user = authService.getUserByEmail(authentication.getName());
        QrCodeResponse qrCode = paymentService.generateQrCode(user.getId());
        return ResponseEntity.ok(qrCode);
    }

    @PostMapping("/confirm")
    @Operation(summary = "Confirm payment completion")
    public ResponseEntity<Map<String, Object>> confirmPayment(
            @RequestParam("paymentReferenceId") String paymentReferenceId,
            Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        paymentService.confirmPayment(paymentReferenceId, user.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Payment confirmed successfully");
        return ResponseEntity.ok(response);
    }
}
