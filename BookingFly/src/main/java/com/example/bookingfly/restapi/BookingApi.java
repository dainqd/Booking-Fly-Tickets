package com.example.bookingfly.restapi;

import com.example.bookingfly.dto.BookingDto;
import com.example.bookingfly.entity.Booking;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.service.BookingService;
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
@RequestMapping("/api/bookings/")
@RequiredArgsConstructor
public class BookingApi {
    final BookingService bookingService;
    final MessageResourceService messageResourceService;
    final UserDetailsServiceImpl userDetailsService;

    @GetMapping("list")
    public Page<BookingDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                    @RequestParam(value = "status", required = false, defaultValue = "10") Enums.BookingStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (status != Enums.BookingStatus.DELETED) {
            return bookingService.findAllByStatus(status, pageable).map(BookingDto::new);
        } else {
            return bookingService.findAllByStatus(Enums.BookingStatus.PROCESSING, pageable).map(BookingDto::new);
        }
    }


    @GetMapping("list/{id}")
    public Page<BookingDto> getList(@PathVariable("id") Long id,
                                    @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                    @RequestParam(value = "status", required = false, defaultValue = "10") Enums.BookingStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Optional<User> optionalUser = userDetailsService.findById(id);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("account.not.found"));
        }
        if (status != Enums.BookingStatus.DELETED) {
            return bookingService.findAllByUserAndStatus(optionalUser.get(), status, pageable).map(BookingDto::new);
        } else {
            return bookingService.findAllByUserAndStatus(optionalUser.get(), Enums.BookingStatus.PROCESSING, pageable).map(BookingDto::new);
        }
    }

    @GetMapping("{id}/{status}")
    public BookingDto getDetail(@PathVariable("id") Long id, @PathVariable("status") Enums.BookingStatus status) {
        Optional<Booking> optionalBooking;
        if (status != Enums.BookingStatus.DELETED) {
            optionalBooking = bookingService.findByIdAndStatus(id, status);
        } else {
            optionalBooking = bookingService.findByIdAndStatus(id, Enums.BookingStatus.PROCESSING);
        }
        if (!optionalBooking.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("flights.not.found"));
        }
        return new BookingDto(optionalBooking.get());
    }

    @PostMapping("create")
    public BookingDto create(@RequestBody BookingDto bookingDto) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("account.not.found"));
        }
        User admin = optionalUser.get();
        return new BookingDto(bookingService.create(bookingDto, admin.getId()));
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancel(@PathVariable("id") Long id) {
        bookingService.cancelBooking(id);
        return new ResponseEntity<>(messageResourceService.getMessage("cancel.success"), HttpStatus.OK);
    }
}
