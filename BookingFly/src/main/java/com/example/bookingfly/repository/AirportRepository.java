package com.example.bookingfly.repository;

import com.example.bookingfly.entity.Airports;
import com.example.bookingfly.util.Enums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airports, Long> {
    Page<Airports> findAll(Pageable pageable);

    Page<Airports> findAllByStatus(Enums.AirportStatus status, Pageable pageable);

    Optional<Airports> findById(long id);

    Optional<Airports> findByIdAndStatus(long id, Enums.AirportStatus status);

    Optional<Airports> findByName(String name);

    Optional<Airports> findByNameAndStatus(String name, Enums.AirportStatus status);
}
