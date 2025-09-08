package com.example.tennisbokker.dto;

import com.example.tennisbokker.entity.enums.SurfaceType;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CourtCreateRequest(
        @NotBlank String name,
        SurfaceType surfaceType,
        BigDecimal priceSingle,
        BigDecimal priceDouble
) {
}
