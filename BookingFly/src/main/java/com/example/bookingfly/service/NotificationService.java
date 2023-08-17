package com.example.bookingfly.service;

import com.example.bookingfly.dto.NotificationDto;
import com.example.bookingfly.entity.Notifications;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.repository.NotificationRepository;
import com.example.bookingfly.util.Enums;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    final NotificationRepository notificationRepository;
    final MessageResourceService messageResourceService;

    final UserService userService;

    public Page<Notifications> findAll(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    public Optional<Notifications> findById(long id) {
        return notificationRepository.findById(id);
    }

    public Notifications save(Notifications notifications) {
        return notificationRepository.save(notifications);
    }

    public Notifications create(NotificationDto notificationDto, long adminId) {
//        try {
            Notifications notifications = new Notifications();

            BeanUtils.copyProperties(notificationDto, notifications);
            notifications.setCreatedAt(LocalDateTime.now());
            notifications.setCreatedBy(adminId);

            getAttributeNotifications(notifications, notificationDto);

            return notificationRepository.save(notifications);
//        } catch (Exception exception) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//                    messageResourceService.getMessage("create.error"));
//        }
    }

    public void deleteById(long id, long adminID) {
        try {
            Optional<Notifications> notifications = notificationRepository.findById(id);
            if (!notifications.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("notifications.not.found"));
            }
            notifications.get().setStatus(Enums.NotificationStatus.DELETED);
            notifications.get().setDeletedAt(LocalDateTime.now());
            notifications.get().setDeletedBy(adminID);
            notificationRepository.save(notifications.get());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("cancel.error"));
        }
    }

    public void seen(long id) {
        try {
            Optional<Notifications> notifications = notificationRepository.findById(id);
            if (!notifications.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("notifications.not.found"));
            }
            notifications.get().setStatus(Enums.NotificationStatus.SEEN);
            notificationRepository.save(notifications.get());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("cancel.error"));
        }
    }

    public Page<Notifications> findAllByStatus(Enums.NotificationStatus status, Pageable pageable) {
        return notificationRepository.findAllByStatus(status, pageable);
    }

    public Optional<Notifications> findByIdAndStatus(long id, Enums.NotificationStatus status) {
        return notificationRepository.findByIdAndStatus(id, status);
    }

    public Page<Notifications> findAllByUser(User user, Pageable pageable) {
        return notificationRepository.findAllByUser(user, pageable);
    }

    public Optional<Notifications> findByIdAndUser(long id, User user) {
        return notificationRepository.findByIdAndUser(id, user);
    }

    private void getAttributeNotifications(Notifications notifications, NotificationDto notificationDto) {
        Optional<User> optionalUser = userService.findById(notificationDto.getUser().getId());
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("account.not.found"));
        }
        notifications.setUser(optionalUser.get());
    }
}
