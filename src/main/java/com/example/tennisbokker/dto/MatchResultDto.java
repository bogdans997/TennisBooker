package com.example.tennisbokker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResultDto {
    private UUID id;
    private UUID appointmentId;
    private String resultText;
    private String photoUrl;
}