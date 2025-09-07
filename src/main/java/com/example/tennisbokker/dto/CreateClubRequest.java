package com.example.tennisbokker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClubRequest {
    private String name;
    private String location;
    private String workingHours;
    private String description;
    private UUID ownerId; // Just the ID of the owner
}

