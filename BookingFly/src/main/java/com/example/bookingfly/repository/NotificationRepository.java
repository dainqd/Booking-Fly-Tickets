package com.example.bookingfly.repository;

import com.example.bookingfly.entity.Notifications;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.util.Enums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {
    Page<Notifications> findAll(Pageable pageable);

    Page<Notifications> findAllByStatus(Enums.NotificationStatus status, Pageable pageable);

    Optional<Notifications> findById(long id);

    Optional<Notifications> findByIdAndStatus(long id, Enums.NotificationStatus status);

    Optional<Notifications> findByIdAndUser(long id, User user);
    Page<Notifications> findAllByUser(User user, Pageable pageable);
}
