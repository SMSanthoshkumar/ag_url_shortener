package com.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeResponse {
    private String qrCodeBase64;
    private String paymentReferenceId;
    private Integer amount;
    private String upiId;
    private String merchantName;
}
