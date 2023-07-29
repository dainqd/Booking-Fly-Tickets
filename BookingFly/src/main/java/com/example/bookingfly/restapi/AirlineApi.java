package com.example.bookingfly.restapi;

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
@RequestMapping("/api/airlines/")
@RequiredArgsConstructor
public class AirlineApi {
    final AirlineService airlineService;
    final MessageResourceService messageResourceService;

    @GetMapping("list")
    public Page<AirlineDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return airlineService.findAllByStatus(Enums.AirlineStatus.ACTIVE, pageable).map(AirlineDto::new);
    }

    @GetMapping("list-area")
    public Page<AirlineDto> getListByArea(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                          @RequestParam(value = "area", required = false, defaultValue = "0") Enums.AirlineArea area) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (area == null) {
            return airlineService.findAllByStatusAndArea(Enums.AirlineStatus.ACTIVE, Enums.AirlineArea.INLAND, pageable).map(AirlineDto::new);
        }
        return airlineService.findAllByStatusAndArea(Enums.AirlineStatus.ACTIVE, area, pageable).map(AirlineDto::new);
    }

    @GetMapping("{id}")
    public AirlineDto getDetail(@PathVariable("id") Long id) {
        Optional<Airlines> optionalAirlines = airlineService.findByIdAndStatus(id, Enums.AirlineStatus.ACTIVE);
        if (!optionalAirlines.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("airlines.not.found"));
        }
        return new AirlineDto(optionalAirlines.get());
    }
}
