package com.example.tennisbokker.dto;

import com.example.tennisbokker.entity.enums.SurfaceType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CourtUpdateRequest(
        @NotBlank String name,
        SurfaceType surfaceType,
        @DecimalMin("0.0") BigDecimal priceSingle,
        @DecimalMin("0.0") BigDecimal priceDouble
) {
}
