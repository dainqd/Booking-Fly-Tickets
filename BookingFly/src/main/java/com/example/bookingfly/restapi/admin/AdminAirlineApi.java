package com.example.bookingfly.restapi.admin;

import com.example.bookingfly.dto.AirlineDto;
import com.example.bookingfly.entity.Airlines;
import com.example.bookingfly.service.AirlineService;
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
@RequestMapping("/admin/api/airlines/")
@RequiredArgsConstructor
public class AdminAirlineApi {
    final AirlineService airlineService;
    final MessageResourceService messageResourceService;

    @GetMapping("")
    public Page<AirlineDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "status", required = false, defaultValue = "") Enums.AirlineStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (status != null) {
            return airlineService.findAllByStatus(status, pageable).map(AirlineDto::new);
        }
        return airlineService.findAll(pageable).map(AirlineDto::new);
    }

    @GetMapping("{id}/{status}")
    public AirlineDto getDetail(@PathVariable(name = "id") Long id, @PathVariable(name = "status") Enums.AirlineStatus status) {
        if (status != null) {
            Optional<Airlines> optionalAirlines = airlineService.findByIdAndStatus(id, status);
            if (!optionalAirlines.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        messageResourceService.getMessage("airlines.not.found"));
            }
            return new AirlineDto(optionalAirlines.get());
        }
        Optional<Airlines> optionalAirlines = airlineService.findById(id);
        if (!optionalAirlines.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("airlines.not.found"));
        }
        return new AirlineDto(optionalAirlines.get());
    }
}
