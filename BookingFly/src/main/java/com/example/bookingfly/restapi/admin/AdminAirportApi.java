package com.example.bookingfly.restapi.admin;

import com.example.bookingfly.dto.AirportDto;
import com.example.bookingfly.entity.Airports;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.service.AirportService;
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
@RequestMapping("/admin/api/airports/")
@RequiredArgsConstructor
public class AdminAirportApi {
    final AirportService airportService;
    final MessageResourceService messageResourceService;
    final UserDetailsServiceImpl userDetailsService;

    @GetMapping("")
    public Page<AirportDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                    @RequestParam(value = "status", required = false, defaultValue = "") Enums.AirportStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (status != null) {
            return airportService.findAllByStatus(status, pageable).map(AirportDto::new);
        }
        return airportService.findAll(pageable).map(AirportDto::new);
    }

    @GetMapping("{id}/{status}")
    public AirportDto getDetail(@PathVariable(name = "id") Long id, @PathVariable(name = "status") Enums.AirportStatus status) {
        if (status != null) {
            Optional<Airports> optionalAirports = airportService.findByIdAndStatus(id, status);
            if (!optionalAirports.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        messageResourceService.getMessage("airports.not.found"));
            }
            return new AirportDto(optionalAirports.get());
        }
        Optional<Airports> optionalAirports = airportService.findById(id);
        if (!optionalAirports.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("airports.not.found"));
        }
        return new AirportDto(optionalAirports.get());
    }

    @PostMapping("")
    public AirportDto create(@RequestBody AirportDto airportDto) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        return new AirportDto(airportService.create(airportDto, user.getId()));
    }

    @PutMapping("")
    public String update(@RequestBody AirportDto request) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        airportService.update(request, user.getId());
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
        airportService.deleteById(id, user.getId());
        return new ResponseEntity<>(messageResourceService.getMessage("delete.success"), HttpStatus.OK);
    }
}
