package com.example.bookingfly.repository;

import com.example.bookingfly.entity.Airports;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.util.Enums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /*
     * Find user by username
     * @param username
     * @return user
     * */
    Optional<User> findByUsername(String username);

    /*
     * Check exits an user by username
     * @param username
     * @return user
     * */
    Boolean existsByUsername(String username);

    /*
     * Check exits by user email
     * @param email
     * @return user
     * */
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Page<User> findAllByStatus(Enums.AccountStatus status, Pageable pageable);

    Optional<User> findById(Long id);

    Optional<User> findByIdAndStatus(Long id, Enums.AccountStatus status);

    Optional<User> findByUsernameAndStatus(String username, Enums.AccountStatus status);
}