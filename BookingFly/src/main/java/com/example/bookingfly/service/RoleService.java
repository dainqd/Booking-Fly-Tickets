package com.example.bookingfly.service;

import com.example.bookingfly.entity.Role;
import com.example.bookingfly.repository.RoleRepository;
import com.example.bookingfly.util.Enums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public Optional<Role> findByName(Enums.Role name) {
        return roleRepository.findByName(name);
    }

    public Role save(Role srole) {
        return roleRepository.save(srole);
    }
}