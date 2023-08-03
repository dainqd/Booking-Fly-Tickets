package com.example.bookingfly.service;

import com.example.bookingfly.dto.PassengerDto;
import com.example.bookingfly.entity.Booking;
import com.example.bookingfly.entity.Passengers;
import com.example.bookingfly.repository.PassengerRepository;
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
public class PassengerService {
    final MessageResourceService messageResourceService;
    final PassengerRepository passengerRepository;
    final BookingService bookingService;

    public Page<Passengers> findAll(Pageable pageable) {
        return passengerRepository.findAll(pageable);
    }

    public Optional<Passengers> findById(long id) {
        return passengerRepository.findById(id);
    }

    public Passengers save(Passengers passengers) {
        return passengerRepository.save(passengers);
    }

    public Passengers create(PassengerDto passengerDto, long adminId) {
        try {
            Passengers passengers = new Passengers();

            BeanUtils.copyProperties(passengerDto, passengers);
            passengers.setCreatedAt(LocalDateTime.now());
            passengers.setCreatedBy(adminId);

            Optional<Booking> optionalBooking = bookingService.findById(passengerDto.getBooking().getId());
            if (!optionalBooking.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("booking.not.found"));
            }
            passengers.setBooking(optionalBooking.get());
            return passengerRepository.save(passengers);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("create.error"));

        }
    }

    public Passengers update(PassengerDto passengerDto, long adminID) {
        try {
            Optional<Passengers> optionalPassengers = passengerRepository.findById(passengerDto.getId());
            if (!optionalPassengers.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("passengers.not.found"));
            }
            Passengers passengers = optionalPassengers.get();

            BeanUtils.copyProperties(passengerDto, passengers);
            passengers.setUpdatedAt(LocalDateTime.now());
            passengers.setUpdatedBy(adminID);

            Optional<Booking> optionalBooking = bookingService.findById(passengerDto.getBooking().getId());
            if (!optionalBooking.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("booking.not.found"));
            }
            passengers.setBooking(optionalBooking.get());
            return passengerRepository.save(passengers);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("update.error"));
        }
    }

    public void deleteById(long id, long adminID) {
        try {
            Optional<Passengers> optionalPassengers = passengerRepository.findById(id);
            if (!optionalPassengers.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("passengers.not.found"));
            }
            optionalPassengers.get().setStatus(Enums.PassengerStatus.DELETED);
            optionalPassengers.get().setDeletedAt(LocalDateTime.now());
            optionalPassengers.get().setDeletedBy(adminID);
            passengerRepository.save(optionalPassengers.get());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("delete.error"));
        }
    }

    public Page<Passengers> findAllByStatus(Enums.PassengerStatus status, Pageable pageable) {
        return passengerRepository.findAllByStatus(status, pageable);
    }

    public Optional<Passengers> findByIdAndStatus(long id, Enums.PassengerStatus status) {
        return passengerRepository.findByIdAndStatus(id, status);
    }
}
