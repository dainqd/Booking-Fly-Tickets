package com.example.bookingfly.restapi.admin;

import com.example.bookingfly.dto.AirlineDto;
import com.example.bookingfly.entity.Airlines;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.service.AirlineService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/admin/api/airlines/")
@RequiredArgsConstructor
public class AdminAirlineApi {
    final AirlineService airlineService;
    final MessageResourceService messageResourceService;
    final UserDetailsServiceImpl userDetailsService;

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

    @GetMapping("list-area")
    public Page<AirlineDto> getListByArea(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                          @RequestParam(value = "area", required = false, defaultValue = "0") Enums.AirlineArea area) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (area == null) {
            return airlineService.findAll(pageable).map(AirlineDto::new);
        }
        return airlineService.findAllByArea(area, pageable).map(AirlineDto::new);
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

    @PostMapping("")
    public AirlineDto create(@RequestBody AirlineDto airlineDto) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        return new AirlineDto(airlineService.create(airlineDto, user.getId()));
    }

    @PutMapping("")
    public String update(@RequestBody AirlineDto request) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        airlineService.update(request, user.getId());
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
        airlineService.deleteById(id, user.getId());
        return new ResponseEntity<>(messageResourceService.getMessage("delete.success"), HttpStatus.OK);
    }
}
