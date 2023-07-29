package com.example.bookingfly.restapi;

import com.example.bookingfly.dto.AirlineDto;
import com.example.bookingfly.dto.FlightDto;
import com.example.bookingfly.entity.Flights;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.service.FlightService;
import com.example.bookingfly.service.MessageResourceService;
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
@RequestMapping("/api/flights/")
@RequiredArgsConstructor
public class FlightApi {
    final MessageResourceService messageResourceService;
    final FlightService flightService;

    @GetMapping("list")
    public Page<FlightDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                   @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return flightService.findAllByStatus(Enums.FlightStatus.ACTIVE, pageable).map(FlightDto::new);
    }

    @GetMapping("{id}")
    public FlightDto getDetail(@PathVariable("id") Long id) {
        Optional<Flights> optionalFlights = flightService.findByIdAndStatus(id, Enums.FlightStatus.ACTIVE);
        if (!optionalFlights.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("flights.not.found"));
        }
        return new FlightDto(optionalFlights.get());
    }
}
