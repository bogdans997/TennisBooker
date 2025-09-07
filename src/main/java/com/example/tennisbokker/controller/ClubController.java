package com.example.tennisbokker.controller;

import com.example.tennisbokker.dto.ResponseClubDto;
import com.example.tennisbokker.dto.CreateClubRequest;
import com.example.tennisbokker.entity.Club;
import com.example.tennisbokker.service.ClubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping("/{id}")
    public ResponseClubDto getClubById(@PathVariable UUID id) {
        return clubService.findById(id);
    }

    @GetMapping
    public List<ResponseClubDto> getAllClubs() {
        return clubService.findAll();
    }

    @PostMapping
    public ResponseEntity<Club> createClub(@RequestBody CreateClubRequest createClubRequest) {
        Club created = clubService.create(createClubRequest);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Club> updateClub(@PathVariable UUID id, @RequestBody Club club) {
        Club updated = clubService.update(id, club);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable UUID id) {
        clubService.delete(id);
        return ResponseEntity.noContent().build();
    }
}