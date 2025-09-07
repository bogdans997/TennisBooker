package com.example.tennisbokker.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ResponseCourtDto(
        UUID id,
        String name,
        String surfaceType,
        BigDecimal priceSingle,
        BigDecimal priceDouble,
        UUID clubId,
        String clubName
) {
}
