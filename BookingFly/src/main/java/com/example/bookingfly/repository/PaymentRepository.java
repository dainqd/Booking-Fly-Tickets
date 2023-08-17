package com.example.bookingfly.repository;

import com.example.bookingfly.entity.Payments;
import com.example.bookingfly.util.Enums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payments, Long> {
    Page<Payments> findAll(Pageable pageable);

    Page<Payments> findAllByStatus(Enums.PaymentStatus status, Pageable pageable);

    Optional<Payments> findById(long id);

    Optional<Payments> findByIdAndStatus(long id, Enums.PaymentStatus status);
}
