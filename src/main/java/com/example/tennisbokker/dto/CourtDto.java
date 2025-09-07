package com.example.tennisbokker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourtDto {
    private UUID id;
    private String name;
    private String surfaceType;
    private BigDecimal priceSingle;
    private BigDecimal priceDouble;
    private UUID clubId;
}