package com.example.tennisbokker.dto;

import com.example.tennisbokker.entity.enums.SurfaceType;

import java.math.BigDecimal;
import java.util.UUID;

public record CourtResponseDto(
        UUID id,
        String name,
        SurfaceType surfaceType,
        BigDecimal priceSingle,
        BigDecimal priceDouble,
        UUID clubId,
        String clubName
) {
}
