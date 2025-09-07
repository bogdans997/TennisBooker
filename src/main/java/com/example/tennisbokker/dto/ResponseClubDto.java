package com.example.tennisbokker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseClubDto {
    private UUID id;
    private String name;
    private String location;
    private String workingHours;
    private String description;
    private String ownerFullName;
}