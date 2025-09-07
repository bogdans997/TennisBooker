package com.example.tennisbokker.repository;

import com.example.tennisbokker.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClubRepository extends JpaRepository<Club, UUID> {

    @Query("SELECT c FROM Club c JOIN FETCH c.owner")
    List<Club> findAllWithOwner();

    @Query("SELECT c FROM Club c JOIN FETCH c.owner where c.id = :id")
    Optional<Club> findByIdWithOwner(@org.springframework.data.repository.query.Param("id") UUID id);
}