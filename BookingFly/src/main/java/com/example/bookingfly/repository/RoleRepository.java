package com.example.bookingfly.repository;

import com.example.bookingfly.entity.Role;
import com.example.bookingfly.util.Enums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /*
     * Find role by name
     * @param name
     * return role
     * */
    Optional<Role> findByName(Enums.Role name);
}
