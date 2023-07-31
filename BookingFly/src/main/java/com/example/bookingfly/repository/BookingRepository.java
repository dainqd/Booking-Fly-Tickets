package com.example.bookingfly.repository;

import com.example.bookingfly.entity.Booking;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.util.Enums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAll(Pageable pageable);

    Page<Booking> findAllByStatus(Enums.BookingStatus status, Pageable pageable);

    Optional<Booking> findById(long id);

    Optional<Booking> findByIdAndStatus(long id, Enums.BookingStatus status);

    Page<Booking> findAllByUserAndStatus(User user, Enums.BookingStatus status, Pageable pageable);

    Page<Booking> findAllByUser(User user, Pageable pageable);
}
