package com.example.bookingfly.restapi;

import com.example.bookingfly.dto.AirportDto;
import com.example.bookingfly.entity.Airports;
import com.example.bookingfly.service.AirportService;
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
@RequestMapping("/api/airports/")
@RequiredArgsConstructor
public class AirportApi {
    final AirportService airportService;
    final MessageResourceService messageResourceService;

    @GetMapping("list")
    public Page<AirportDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return airportService.findAllByStatus(Enums.AirportStatus.ACTIVE, pageable).map(AirportDto::new);
    }

    @GetMapping("{id}")
    public AirportDto getDetail(@PathVariable("id") Long id) {
        Optional<Airports> optionalAirports = airportService.findByIdAndStatus(id, Enums.AirportStatus.ACTIVE);
        if (!optionalAirports.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("airports.not.found"));
        }
        return new AirportDto(optionalAirports.get());
    }
}
