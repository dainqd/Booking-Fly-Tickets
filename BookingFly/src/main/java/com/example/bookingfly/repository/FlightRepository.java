package com.example.bookingfly.repository;

import com.example.bookingfly.entity.Flights;
import com.example.bookingfly.util.Enums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flights, Long> {
    Page<Flights> findAll(Pageable pageable);

    Page<Flights> findAllByStatus(Enums.FlightStatus status, Pageable pageable);

    Optional<Flights> findById(long id);

    Optional<Flights> findByIdAndStatus(long id, Enums.FlightStatus status);
}
