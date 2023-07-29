package com.example.bookingfly.restapi.admin;

import com.example.bookingfly.dto.FlightDto;
import com.example.bookingfly.entity.Flights;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.service.FlightService;
import com.example.bookingfly.service.MessageResourceService;
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
@RequestMapping("/admin/api/flights/")
@RequiredArgsConstructor
public class AdminFlightApi {
    final MessageResourceService messageResourceService;
    final FlightService flightService;
    final UserDetailsServiceImpl userDetailsService;

    @GetMapping("")
    public Page<FlightDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                   @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                   @RequestParam(value = "status", required = false, defaultValue = "") Enums.FlightStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (status != null) {
            return flightService.findAllByStatus(status, pageable).map(FlightDto::new);
        }
        return flightService.findAll(pageable).map(FlightDto::new);
    }

    @GetMapping("{id}/{status}")
    public FlightDto getDetail(@PathVariable(name = "id") Long id, @PathVariable(name = "status") Enums.FlightStatus status) {
        if (status != null) {
            Optional<Flights> optionalFlights = flightService.findByIdAndStatus(id, status);
            if (!optionalFlights.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        messageResourceService.getMessage("flights.not.found"));
            }
            return new FlightDto(optionalFlights.get());
        }
        Optional<Flights> optionalFlights = flightService.findById(id);
        if (!optionalFlights.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("flights.not.found"));
        }
        return new FlightDto(optionalFlights.get());
    }

    @PostMapping("")
    public FlightDto create(@RequestBody FlightDto flightDto) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        return new FlightDto(flightService.create(flightDto, user.getId()));
    }

    @PutMapping("")
    public String update(@RequestBody FlightDto request) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        flightService.update(request, user.getId());
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
        flightService.deleteById(id, user.getId());
        return new ResponseEntity<>(messageResourceService.getMessage("delete.success"), HttpStatus.OK);
    }
}
