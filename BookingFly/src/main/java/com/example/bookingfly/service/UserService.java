package com.example.bookingfly.service;

import com.example.bookingfly.dto.UserDto;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.repository.RoleRepository;
import com.example.bookingfly.repository.UserRepository;
import com.example.bookingfly.util.Enums;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final PasswordEncoder encoder;
    final MessageResourceService messageResourceService;

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User create(UserDto userDto, long adminId) {
        try {
            User user = new User();
            BeanUtils.copyProperties(userDto, user);
            user.setPassword(encoder.encode(userDto.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            user.setCreatedBy(adminId);
            return userRepository.save(user);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("create.error"));

        }
    }

    public User update(UserDto userDto, long adminID) {
        try {
            Optional<User> optionalUser = userRepository.findById(userDto.getId());
            if (!optionalUser.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("account.not.found"));
            }
            User user = optionalUser.get();

            BeanUtils.copyProperties(userDto, user);
            user.setUpdatedAt(LocalDateTime.now());
            user.setUpdatedBy(adminID);
            return userRepository.save(user);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("update.error"));
        }
    }

    public void deleteById(long id, long adminID) {
        try {
            Optional<User> airports = userRepository.findById(id);
            if (!airports.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("account.not.found"));
            }
            airports.get().setStatus(Enums.AccountStatus.DELETED);
            airports.get().setDeletedAt(LocalDateTime.now());
            airports.get().setDeletedBy(adminID);
            userRepository.save(airports.get());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("cancel.error"));
        }
    }

    public Page<User> findAllByStatus(Enums.AccountStatus status, Pageable pageable) {
        return userRepository.findAllByStatus(status, pageable);
    }

    public Optional<User> findByIdAndStatus(long id, Enums.AccountStatus status) {
        return userRepository.findByIdAndStatus(id, status);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
