package com.example.tennisbokker.repository;

import com.example.tennisbokker.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourtRepository extends JpaRepository<Court, UUID> {
}