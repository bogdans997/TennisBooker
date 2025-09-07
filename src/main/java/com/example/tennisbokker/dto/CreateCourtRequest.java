package com.example.tennisbokker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

public record CreateCourtRequest(
        @NotBlank String name,
        String surfaceType,
        BigDecimal priceSingle,
        BigDecimal priceDouble
) {
}
