package com.example.tennisbokker.repository;

import com.example.tennisbokker.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClubRepository extends JpaRepository<Club, UUID> {

    Optional<Club> findById(UUID id);

    boolean existsByOwner_IdAndNameNormalized(UUID ownerId, String nameNormalized);

    @Query("""
            SELECT c FROM Club c
            WHERE (:ownerId IS NULL OR c.owner.id = :ownerId)
              AND (
                  :q IS NULL
                  OR LOWER(c.name) LIKE LOWER(CONCAT('%', :q, '%'))
                  OR LOWER(c.location) LIKE LOWER(CONCAT('%', :q, '%'))
              )
            """)
    Page<Club> search(
            @Param("ownerId") UUID ownerId,
            @Param("q") String q,
            Pageable pageable
    );
}