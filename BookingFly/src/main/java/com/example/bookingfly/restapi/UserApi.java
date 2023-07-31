package com.example.bookingfly.restapi;

import com.example.bookingfly.dto.UserDto;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.service.MessageResourceService;
import com.example.bookingfly.service.UserDetailsServiceImpl;

import com.example.bookingfly.service.UserService;
import com.example.bookingfly.util.Enums;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserApi {
    final UserDetailsServiceImpl userDetailsServiceimpl;
    final MessageResourceService messageResourceService;
    final UserService userService;

    @GetMapping("")
    public Page<UserDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                 @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return userService.findAllByStatus(Enums.AccountStatus.ACTIVE, pageable).map(UserDto::new);
    }

    @GetMapping("{id}")
    public UserDto getDetail(@PathVariable(name = "id") Long id) {
        Optional<User> optionalUser;
        optionalUser = userService.findByIdAndStatus(id, Enums.AccountStatus.ACTIVE);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("account.not.found"));
        }
        return new UserDto(optionalUser.get());
    }
}
