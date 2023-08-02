package com.example.bookingfly.restapi.admin;

import com.example.bookingfly.dto.PassengerDto;
import com.example.bookingfly.entity.Passengers;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.service.MessageResourceService;
import com.example.bookingfly.service.PassengerService;
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
@RequestMapping("/admin/api/passengers/")
@RequiredArgsConstructor
public class AdminPassengerApi {
    final PassengerService passengerService;
    final MessageResourceService messageResourceService;
    final UserService userService;

    @GetMapping("")
    public Page<PassengerDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                      @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                      @RequestParam(value = "status", required = false, defaultValue = "") Enums.PassengerStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (status != null) {
            return passengerService.findAllByStatus(status, pageable).map(PassengerDto::new);
        }
        return passengerService.findAll(pageable).map(PassengerDto::new);
    }

    @GetMapping("{id}/{status}")
    public PassengerDto getDetail(@PathVariable(name = "id") Long id, @PathVariable(name = "status") Enums.PassengerStatus status) {
        Optional<Passengers> optionalPassengers;
        if (status != null) {
            optionalPassengers = passengerService.findByIdAndStatus(id, status);
        } else {
            optionalPassengers = passengerService.findById(id);
        }
        if (!optionalPassengers.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("account.not.found"));
        }
        return new PassengerDto(optionalPassengers.get());
    }

    @PostMapping("")
    public PassengerDto create(@RequestBody PassengerDto passengerDto) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        return new PassengerDto(passengerService.create(passengerDto, user.getId()));
    }

    @PutMapping("")
    public String update(@RequestBody PassengerDto request) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        passengerService.update(request, user.getId());
        return messageResourceService.getMessage("update.success");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        passengerService.deleteById(id, user.getId());
        return new ResponseEntity<>(messageResourceService.getMessage("delete.success"), HttpStatus.OK);
    }
}
