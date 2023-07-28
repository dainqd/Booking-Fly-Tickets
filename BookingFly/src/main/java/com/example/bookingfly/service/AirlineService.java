package com.example.bookingfly.service;

import com.example.bookingfly.dto.AirlineDto;
import com.example.bookingfly.entity.Airlines;
import com.example.bookingfly.repository.AirlineRepository;
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
public class AirlineService {
    final AirlineRepository airlineRepository;
    final MessageResourceService messageResourceService;

    public Page<Airlines> findAll(Pageable pageable) {
        return airlineRepository.findAll(pageable);
    }

    public Optional<Airlines> findById(long id) {
        return airlineRepository.findById(id);
    }

    public Airlines save(Airlines airlines) {
        return airlineRepository.save(airlines);
    }

    public Airlines create(AirlineDto airlineDto, long adminId) {
        try {
            Airlines airlines = new Airlines();
            BeanUtils.copyProperties(airlineDto, airlines);
            airlines.setCreatedAt(LocalDateTime.now());
            airlines.setCreatedBy(adminId);
            return airlineRepository.save(airlines);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("create.error"));
        }
    }

    public Airlines update(AirlineDto airlineDto, long adminID) {
        try {
            Optional<Airlines> optionalAirlines = airlineRepository.findById(airlineDto.getId());
            if (!optionalAirlines.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("airlines.not.found"));
            } else {
                Airlines airlines = optionalAirlines.get();
                BeanUtils.copyProperties(airlineDto, airlines);
                airlines.setUpdatedAt(LocalDateTime.now());
                airlines.setUpdatedBy(adminID);
                return airlineRepository.save(airlines);
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("update.error"));
        }
    }

    public void deleteById(long id, long adminID) {
        try {
            Optional<Airlines> airlines = airlineRepository.findById(id);
            if (!airlines.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("airlines.not.found"));
            }
            airlines.get().setStatus(Enums.AirlineStatus.DELETED);
            airlines.get().setDeletedAt(LocalDateTime.now());
            airlines.get().setDeletedBy(adminID);
            airlineRepository.save(airlines.get());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("delete.error"));
        }
    }

    public Page<Airlines> findAllByStatus(Enums.AirlineStatus status, Pageable pageable) {
        return airlineRepository.findAllByStatus(status, pageable);
    }

    public Optional<Airlines> findByIdAndStatus(long id, Enums.AirlineStatus status) {
        return airlineRepository.findByIdAndStatus(id, status);
    }

    public Optional<Airlines> findByName(String name) {
        return airlineRepository.findByName(name);
    }

    public Optional<Airlines> findByNameAndStatus(String name, Enums.AirlineStatus status) {
        return airlineRepository.findByNameAndStatus(name, status);
    }
}
