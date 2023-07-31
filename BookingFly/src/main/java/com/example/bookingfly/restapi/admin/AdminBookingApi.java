package com.example.bookingfly.restapi.admin;

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
@RequestMapping("/admin/api/bookings/")
@RequiredArgsConstructor
public class AdminBookingApi {
    final BookingService bookingService;
    final MessageResourceService messageResourceService;
    final UserDetailsServiceImpl userDetailsService;

    @GetMapping("")
    public Page<BookingDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                    @RequestParam(value = "status", required = false, defaultValue = "") Enums.BookingStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (status != null) {
            return bookingService.findAllByStatus(status, pageable).map(BookingDto::new);
        }
        return bookingService.findAll(pageable).map(BookingDto::new);
    }

    @GetMapping("user/{id}/{status}")
    public Page<BookingDto> getListByArea(@PathVariable("id") Long id,
                                          @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                          @RequestParam(value = "area", required = false, defaultValue = "0") Enums.BookingStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Optional<User> optionalUser = userDetailsService.findById(id);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("account.not.found"));
        }
        if (status == null) {
            return bookingService.findAllByUser(optionalUser.get(), pageable).map(BookingDto::new);
        }
        return bookingService.findAllByUserAndStatus(optionalUser.get(), status, pageable).map(BookingDto::new);
    }

    @GetMapping("{id}/{status}")
    public BookingDto getDetail(@PathVariable(name = "id") Long id, @PathVariable(name = "status") Enums.BookingStatus status) {
        Optional<Booking> optionalBooking;
        if (status != null) {
            optionalBooking = bookingService.findByIdAndStatus(id, status);
        } else {
            optionalBooking = bookingService.findById(id);
        }
        if (!optionalBooking.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("booking.not.found"));
        }
        return new BookingDto(optionalBooking.get());
    }

    @PostMapping("/create")
    public BookingDto create(@RequestBody BookingDto bookingDto) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("account.not.found"));
        }
        User user = optionalUser.get();
        return new BookingDto(bookingService.create(bookingDto, user.getId()));
    }

    @PutMapping("")
    public String update(@RequestBody BookingDto request) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userDetailsService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        bookingService.update(request, user.getId());
        return messageResourceService.getMessage("update.success");
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancel(@PathVariable("id") Long id) {
        bookingService.cancelBooking(id);
        return new ResponseEntity<>(messageResourceService.getMessage("cancel.success"), HttpStatus.OK);
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
        bookingService.deleteById(id, user.getId());
        return new ResponseEntity<>(messageResourceService.getMessage("delete.success"), HttpStatus.OK);
    }
}
