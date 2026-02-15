package com.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequest {

    @NotBlank(message = "Original URL is required")
    private String originalUrl;

    @NotBlank(message = "Payment reference ID is required")
    private String paymentReferenceId;
}
