package com.example.bookingfly.service;

import com.example.bookingfly.dto.BookingDto;
import com.example.bookingfly.entity.*;
import com.example.bookingfly.repository.BookingRepository;
import com.example.bookingfly.util.Enums;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    final BookingRepository bookingRepository;
    final MessageResourceService messageResourceService;
    final UserDetailsServiceImpl userDetailsService;
    final FlightService flightService;

    public Page<Booking> findAll(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    public Optional<Booking> findById(long id) {
        return bookingRepository.findById(id);
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking create(BookingDto bookingDto, long adminId) {
        try {
            Booking booking = new Booking();

            BeanUtils.copyProperties(bookingDto, booking);
            booking.setCreatedAt(LocalDateTime.now());
            booking.setCreatedBy(adminId);

            getAttributeBooking(booking, bookingDto);

            return bookingRepository.save(booking);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("create.error"));

        }
    }

    public Booking update(BookingDto bookingDto, long adminID) {
        try {
            Optional<Booking> optionalBooking = bookingRepository.findById(bookingDto.getId());
            if (!optionalBooking.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("booking.not.found"));
            }
            Booking booking = optionalBooking.get();

            BeanUtils.copyProperties(bookingDto, booking);
            booking.setUpdatedAt(LocalDateTime.now());
            booking.setUpdatedBy(adminID);
            return bookingRepository.save(booking);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("update.error"));
        }
    }

    public void deleteById(long id, long adminID) {
        try {
            Optional<Booking> airports = bookingRepository.findById(id);
            if (!airports.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("booking.not.found"));
            }
            airports.get().setStatus(Enums.BookingStatus.DELETED);
            airports.get().setDeletedAt(LocalDateTime.now());
            airports.get().setDeletedBy(adminID);
            bookingRepository.save(airports.get());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("cancel.error"));
        }
    }

    public void cancelBooking(long id) {
        try {
            Optional<Booking> airports = bookingRepository.findById(id);
            if (!airports.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("booking.not.found"));
            }
            airports.get().setStatus(Enums.BookingStatus.CANCELED);
            bookingRepository.save(airports.get());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("cancel.error"));
        }
    }

    public Page<Booking> findAllByStatus(Enums.BookingStatus status, Pageable pageable) {
        return bookingRepository.findAllByStatus(status, pageable);
    }

    public Optional<Booking> findByIdAndStatus(long id, Enums.BookingStatus status) {
        return bookingRepository.findByIdAndStatus(id, status);
    }

    public Page<Booking> findAllByUserAndStatus(User user, Enums.BookingStatus status, Pageable pageable) {
        return bookingRepository.findAllByUserAndStatus(user, status, pageable);
    }

    public Page<Booking> findAllByUser(User user, Pageable pageable) {
        return bookingRepository.findAllByUser(user, pageable);
    }

    private void getAttributeBooking(Booking booking, BookingDto bookingDto) {
        Optional<User> optionalUser = userDetailsService.findById(bookingDto.getUser().getId());
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("account.not.found"));
        }

        Optional<Flights> optionalFlight = flightService.findById(bookingDto.getFlight().getId());
        if (!optionalFlight.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("flights.not.found"));
        }

        booking.setUser(optionalUser.get());
        booking.setFlight(optionalFlight.get());
    }
}
