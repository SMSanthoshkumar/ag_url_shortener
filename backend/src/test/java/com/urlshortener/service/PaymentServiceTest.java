package com.urlshortener.service;

import com.urlshortener.dto.QrCodeResponse;
import com.urlshortener.entity.Payment;
import com.urlshortener.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(paymentService, "upiId", "test@upi");
        ReflectionTestUtils.setField(paymentService, "merchantName", "Test Merchant");
        ReflectionTestUtils.setField(paymentService, "paymentAmount", 100);
        ReflectionTestUtils.setField(paymentService, "currency", "INR");
    }

    @Test
    void generateQrCode_ShouldReturnValidResponse() throws Exception {
        Long userId = 1L;

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        QrCodeResponse response = paymentService.generateQrCode(userId);

        assertNotNull(response);
        assertNotNull(response.getQrCodeBase64());
        assertTrue(response.getPaymentReferenceId().startsWith("PAY-"));
        assertEquals(100, response.getAmount());
        assertEquals("test@upi", response.getUpiId());

        verify(paymentRepository).save(any(Payment.class));
    }
}
