package com.example.bookingfly.repository;

import com.example.bookingfly.entity.Airlines;
import com.example.bookingfly.util.Enums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirlineRepository extends JpaRepository<Airlines, Long> {
    Page<Airlines> findAll(Pageable pageable);

    Page<Airlines> findAllByStatus(Enums.AirlineStatus status, Pageable pageable);

    Page<Airlines> findAllByArea(Enums.AirlineArea area, Pageable pageable);

    Page<Airlines> findAllByStatusAndArea(Enums.AirlineStatus status, Enums.AirlineArea area, Pageable pageable);

    Optional<Airlines> findById(long id);

    Optional<Airlines> findByIdAndStatus(long id, Enums.AirlineStatus status);

    Optional<Airlines> findByName(String name);

    Optional<Airlines> findByNameAndStatus(String name, Enums.AirlineStatus status);
}
