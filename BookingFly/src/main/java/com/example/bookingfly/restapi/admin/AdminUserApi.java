package com.example.bookingfly.restapi.admin;

import com.example.bookingfly.dto.UserDto;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.repository.RoleRepository;
import com.example.bookingfly.service.MessageResourceService;
import com.example.bookingfly.service.UserDetailsServiceImpl;
import com.example.bookingfly.service.UserService;
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
@RequiredArgsConstructor
@RequestMapping("admin/api/user")
public class AdminUserApi {
    final UserService userService;
    final UserDetailsServiceImpl userDetailsService;
    final RoleRepository roleRepository;
    final MessageResourceService messageResourceService;

    @GetMapping("")
    public Page<UserDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                 @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                 @RequestParam(value = "status", required = false, defaultValue = "") Enums.AccountStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (status != null) {
            return userService.findAllByStatus(status, pageable).map(UserDto::new);
        }
        return userService.findAll(pageable).map(UserDto::new);
    }

    @GetMapping("{id}/{status}")
    public UserDto getDetail(@PathVariable(name = "id") Long id, @PathVariable(name = "status") Enums.AccountStatus status) {
        Optional<User> optionalUser;
        if (status != null) {
            optionalUser = userService.findByIdAndStatus(id, status);
        } else {
            optionalUser = userService.findById(id);
        }
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("account.not.found"));
        }
        return new UserDto(optionalUser.get());
    }

    @PostMapping("")
    public UserDto create(@RequestBody UserDto userDto) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        return new UserDto(userService.create(userDto, user.getId()));
    }

    @PutMapping("")
    public String update(@RequestBody UserDto request) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        userService.update(request, user.getId());
        return messageResourceService.getMessage("update.success");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        String username = Utils.getUsername();
        System.out.println(username);
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        userService.deleteById(id, user.getId());
        return new ResponseEntity<>(messageResourceService.getMessage("delete.success"), HttpStatus.OK);
    }
}
