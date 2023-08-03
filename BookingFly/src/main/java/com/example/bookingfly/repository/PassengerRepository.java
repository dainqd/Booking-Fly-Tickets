package com.example.bookingfly.repository;

import com.example.bookingfly.entity.Passengers;
import com.example.bookingfly.util.Enums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passengers, Long> {
    Page<Passengers> findAll(Pageable pageable);

    Page<Passengers> findAllByStatus(Enums.PassengerStatus status, Pageable pageable);

    Optional<Passengers> findById(long id);

    Optional<Passengers> findByIdAndStatus(long id, Enums.PassengerStatus status);
}
