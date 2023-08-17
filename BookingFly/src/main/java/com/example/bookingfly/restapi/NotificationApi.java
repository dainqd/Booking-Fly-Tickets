package com.example.bookingfly.restapi;

import com.example.bookingfly.dto.NotificationDto;
import com.example.bookingfly.entity.Notifications;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.service.MessageResourceService;
import com.example.bookingfly.service.NotificationService;
import com.example.bookingfly.service.UserDetailsServiceImpl;
import com.example.bookingfly.util.Enums;
import com.example.bookingfly.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/notifications/")
@RequiredArgsConstructor
public class NotificationApi {
    final NotificationService notificationService;
    final MessageResourceService messageResourceService;
    final UserDetailsServiceImpl userDetailsService;

    @GetMapping("list")
    public Page<NotificationDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                         @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        String username = Utils.getUsername();
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        return notificationService.findAllByUser(user, pageable).map(NotificationDto::new);
    }

    @GetMapping("{id}")
    public NotificationDto getDetail(@PathVariable("id") Long id) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        Optional<Notifications> optionalNotifications = notificationService.findByIdAndUser(id, user);
        if (!optionalNotifications.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("notifications.not.found"));
        }
        if (optionalNotifications.get().getStatus() == Enums.NotificationStatus.DELETED) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("notifications.not.found"));
        }

        notificationService.seen(id);
        return new NotificationDto(optionalNotifications.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        notificationService.deleteById(id, user.getId());
        return new ResponseEntity<>(messageResourceService.getMessage("delete.success"), HttpStatus.OK);
    }
}
