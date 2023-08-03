package com.example.bookingfly.repository;

import com.example.bookingfly.entity.Reviews;
import com.example.bookingfly.util.Enums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {
    Page<Reviews> findAll(Pageable pageable);

    Page<Reviews> findAllByStatus(Enums.ReviewStatus status, Pageable pageable);

    Optional<Reviews> findById(long id);

    Optional<Reviews> findByIdAndStatus(long id, Enums.ReviewStatus status);

}
