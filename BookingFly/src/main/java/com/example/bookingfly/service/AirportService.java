package com.example.bookingfly.service;

import com.example.bookingfly.dto.AirportDto;
import com.example.bookingfly.entity.Airports;
import com.example.bookingfly.entity.Location;
import com.example.bookingfly.repository.AirportRepository;
import com.example.bookingfly.repository.LocationRepository;
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
public class AirportService {
    final AirportRepository airportRepository;
    final LocationRepository locationRepository;
    final MessageResourceService messageResourceService;

    public Page<Airports> findAll(Pageable pageable) {
        return airportRepository.findAll(pageable);
    }

    public Optional<Airports> findById(long id) {
        return airportRepository.findById(id);
    }

    public Airports save(Airports airports) {
        return airportRepository.save(airports);
    }

    public Airports create(AirportDto airportDto, long adminId) {
        try {
            Airports airports = new Airports();
            Location location = new Location();

            setLocation(location, airportDto);

            BeanUtils.copyProperties(airportDto, airports);
            airports.setCreatedAt(LocalDateTime.now());
            airports.setCreatedBy(adminId);
            airports.setLocation(location);
            return airportRepository.save(airports);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("create.error"));

        }
    }

    public Airports update(AirportDto airportDto, long adminID) {
        try {
            Optional<Airports> optionalAirports = airportRepository.findById(airportDto.getId());
            if (!optionalAirports.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("airports.not.found"));
            }
            Airports airports = optionalAirports.get();

            Optional<Location> optionalLocation = locationRepository.findById(airportDto.getLocation().getId());
            if (!optionalLocation.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("location.not.found"));
            }

            Location location = optionalLocation.get();

            setLocation(location, airportDto);

            BeanUtils.copyProperties(airportDto, airports);
            airports.setUpdatedAt(LocalDateTime.now());
            airports.setUpdatedBy(adminID);
            return airportRepository.save(airports);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("update.error"));
        }
    }

    public void deleteById(long id, long adminID) {
        try {
            Optional<Airports> airports = airportRepository.findById(id);
            if (!airports.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("airports.not.found"));
            }
            airports.get().setStatus(Enums.AirportStatus.DELETED);
            airports.get().setDeletedAt(LocalDateTime.now());
            airports.get().setDeletedBy(adminID);
            airportRepository.save(airports.get());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("delete.error"));
        }
    }

    public Page<Airports> findAllByStatus(Enums.AirportStatus status, Pageable pageable) {
        return airportRepository.findAllByStatus(status, pageable);
    }

    public Optional<Airports> findByIdAndStatus(long id, Enums.AirportStatus status) {
        return airportRepository.findByIdAndStatus(id, status);
    }

    private void setLocation(Location location, AirportDto airportDto) {
        location.setCountry(airportDto.getLocation().getCountry());
        location.setCity(airportDto.getLocation().getCity());
        location.setDistrict(airportDto.getLocation().getDistrict());
        location.setWard(airportDto.getLocation().getWard());
        location.setPlace(airportDto.getLocation().getPlace());
        locationRepository.save(location);
    }
}
