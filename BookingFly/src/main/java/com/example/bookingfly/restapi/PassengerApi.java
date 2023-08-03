package com.example.bookingfly.restapi;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/passengers/")
@RequiredArgsConstructor
public class PassengerApi {
    final PassengerService passengerService;
    final MessageResourceService messageResourceService;
    final UserService userService;

    @GetMapping("list")
    public Page<PassengerDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                      @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return passengerService.findAllByStatus(Enums.PassengerStatus.ACTIVE, pageable).map(PassengerDto::new);
    }

    @GetMapping("{id}")
    public PassengerDto getDetail(@PathVariable("id") Long id) {
        Optional<Passengers> optionalPassengers = passengerService.findByIdAndStatus(id, Enums.PassengerStatus.ACTIVE);
        if (!optionalPassengers.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("flights.not.found"));
        }
        return new PassengerDto(optionalPassengers.get());
    }

    @PostMapping("create")
    public PassengerDto create(@RequestBody PassengerDto passengerDto) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("account.not.found"));
        }
        User admin = optionalUser.get();
        return new PassengerDto(passengerService.create(passengerDto, admin.getId()));
    }
}
