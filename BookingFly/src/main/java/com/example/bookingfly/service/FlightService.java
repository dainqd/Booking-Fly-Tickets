package com.example.bookingfly.service;

import com.example.bookingfly.dto.FlightDto;
import com.example.bookingfly.entity.Airlines;
import com.example.bookingfly.entity.Airports;
import com.example.bookingfly.entity.Flights;
import com.example.bookingfly.repository.AirlineRepository;
import com.example.bookingfly.repository.AirportRepository;
import com.example.bookingfly.repository.FlightRepository;
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
public class FlightService {
    final FlightRepository flightRepository;
    final AirlineRepository airlineRepository;
    final AirportRepository airportRepository;
    final MessageResourceService messageResourceService;

    public Page<Flights> findAll(Pageable pageable) {
        return flightRepository.findAll(pageable);
    }

    public Optional<Flights> findById(long id) {
        return flightRepository.findById(id);
    }

    public Flights save(Flights flights) {
        return flightRepository.save(flights);
    }

    public Flights create(FlightDto flightDto, long adminId) {
        try {
            Flights flights = new Flights();

            BeanUtils.copyProperties(flightDto, flights);
            flights.setCreatedAt(LocalDateTime.now());
            flights.setCreatedBy(adminId);
            getAttributeFlights(flights, flightDto);
            return flightRepository.save(flights);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("create.error"));

        }
    }

    public Flights update(FlightDto flightDto, long adminID) {
        try {
            Optional<Flights> optionalFlights = flightRepository.findById(flightDto.getId());
            if (!optionalFlights.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("flights.not.found"));
            }
            Flights flights = optionalFlights.get();

            BeanUtils.copyProperties(flightDto, flights);
            flights.setUpdatedAt(LocalDateTime.now());
            flights.setUpdatedBy(adminID);

            getAttributeFlights(flights, flightDto);

            return flightRepository.save(flights);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("update.error"));
        }
    }

    public void deleteById(long id, long adminID) {
        try {
            Optional<Flights> optionalFlights = flightRepository.findById(id);
            if (!optionalFlights.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("flights.not.found"));
            }
            optionalFlights.get().setStatus(Enums.FlightStatus.DELETED);
            optionalFlights.get().setDeletedAt(LocalDateTime.now());
            optionalFlights.get().setDeletedBy(adminID);
            flightRepository.save(optionalFlights.get());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("delete.error"));
        }
    }

    public Page<Flights> findAllByStatus(Enums.FlightStatus status, Pageable pageable) {
        return flightRepository.findAllByStatus(status, pageable);
    }

    public Optional<Flights> findByIdAndStatus(long id, Enums.FlightStatus status) {
        return flightRepository.findByIdAndStatus(id, status);
    }

    private void getAttributeFlights(Flights flights, FlightDto flightDto) {
        Optional<Airlines> optionalAirlines = airlineRepository.findById(flightDto.getAirlines().getId());
        if (!optionalAirlines.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("airlines.not.found"));
        }

        Optional<Airports> optionalAirportFroms = airportRepository.findById(flightDto.getFrom_airport_id().getId());
        if (!optionalAirportFroms.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("airports.not.found"));
        }

        Optional<Airports> optionalAirportTos = airportRepository.findById(flightDto.getTo_airport_id().getId());
        if (!optionalAirportTos.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("airports.not.found"));
        }

        flights.setAirlines(optionalAirlines.get());
        flights.setFrom_airport_id(optionalAirportFroms.get());
        flights.setTo_airport_id(optionalAirportTos.get());
    }
}
