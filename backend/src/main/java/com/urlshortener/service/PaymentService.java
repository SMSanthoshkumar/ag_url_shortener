package com.urlshortener.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.urlshortener.dto.QrCodeResponse;
import com.urlshortener.entity.Payment;
import com.urlshortener.exception.ResourceNotFoundException;
import com.urlshortener.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${app.payment.upi-id}")
    private String upiId;

    @Value("${app.payment.merchant-name}")
    private String merchantName;

    @Value("${app.payment.amount}")
    private Integer paymentAmount;

    @Value("${app.payment.currency:INR}")
    private String currency;

    public QrCodeResponse generateQrCode(Long userId) throws WriterException, IOException {
        // Generate unique payment reference ID
        String paymentReferenceId = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Create payment record
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setPaymentReferenceId(paymentReferenceId);
        payment.setAmount(paymentAmount);
        payment.setStatus("PENDING");
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // Generate UPI payment string
        String upiString = createUpiPaymentString(paymentReferenceId);

        // Generate QR code
        String qrCodeBase64 = generateQrCodeImage(upiString);

        return new QrCodeResponse(
                qrCodeBase64,
                paymentReferenceId,
                paymentAmount,
                upiId,
                merchantName);
    }

    private String createUpiPaymentString(String paymentReferenceId) {
        return String.format(
                "upi://pay?pa=%s&pn=%s&am=%s&cu=%s&tn=Payment_%s",
                upiId,
                merchantName.replace(" ", "_"),
                String.format("%.2f", paymentAmount / 100.0), // Convert paise to rupees
                currency,
                paymentReferenceId);
    }

    private String generateQrCodeImage(String upiString) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(upiString, BarcodeFormat.QR_CODE, 300, 300, hints);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Convert to Base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", baos);
        byte[] imageBytes = baos.toByteArray();

        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public void confirmPayment(String paymentReferenceId, Long userId) {
        Payment payment = paymentRepository.findByPaymentReferenceId(paymentReferenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment reference not found"));

        if (!payment.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized payment confirmation");
        }

        if ("CONFIRMED".equals(payment.getStatus())) {
            throw new RuntimeException("Payment already confirmed");
        }

        payment.setStatus("CONFIRMED");
        payment.setConfirmedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }

    public Payment getPaymentByReference(String paymentReferenceId) {
        return paymentRepository.findByPaymentReferenceId(paymentReferenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment reference not found"));
    }
}
