package com.example.bookingfly.service;

import com.example.bookingfly.dto.ReviewDto;
import com.example.bookingfly.entity.Flights;
import com.example.bookingfly.entity.Reviews;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.repository.ReviewRepository;
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
public class ReviewService {
    final ReviewRepository reviewRepository;
    final MessageResourceService messageResourceService;
    final FlightService flightService;
    final UserService userService;

    public Page<Reviews> findAll(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    public Optional<Reviews> findById(long id) {
        return reviewRepository.findById(id);
    }

    public Reviews save(Reviews reviews) {
        return reviewRepository.save(reviews);
    }

    public Reviews create(ReviewDto reviewDto, long adminId) {
        try {
            Reviews reviews = new Reviews();

            BeanUtils.copyProperties(reviewDto, reviews);
            reviews.setCreatedAt(LocalDateTime.now());
            reviews.setCreatedBy(adminId);

            getAttributeReviews(reviews, reviewDto);

            return reviewRepository.save(reviews);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("create.error"));

        }
    }

    public Reviews update(ReviewDto reviewDto, long adminID) {
        try {
            Optional<Reviews> optionalReviews = reviewRepository.findById(reviewDto.getId());
            if (!optionalReviews.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("reviews.not.found"));
            }
            Reviews reviews = optionalReviews.get();

            BeanUtils.copyProperties(reviewDto, reviews);
            reviews.setUpdatedAt(LocalDateTime.now());
            reviews.setUpdatedBy(adminID);
            return reviewRepository.save(reviews);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("update.error"));
        }
    }

    public void deleteById(long id, long adminID) {
        try {
            Optional<Reviews> reviews = reviewRepository.findById(id);
            if (!reviews.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("reviews.not.found"));
            }
            reviews.get().setStatus(Enums.ReviewStatus.DELETED);
            reviews.get().setDeletedAt(LocalDateTime.now());
            reviews.get().setDeletedBy(adminID);
            reviewRepository.save(reviews.get());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("cancel.error"));
        }
    }

    public void toggle(long id, Enums.ReviewStatus status, long adminID) {
        try {
            Optional<Reviews> reviews = reviewRepository.findById(id);
            if (!reviews.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("reviews.not.found"));
            }
            if (status != null) {
                reviews.get().setStatus(Enums.ReviewStatus.ACTIVE);
            } else {
                reviews.get().setStatus(status);
            }
            reviews.get().setDeletedAt(LocalDateTime.now());
            reviews.get().setDeletedBy(adminID);
            reviewRepository.save(reviews.get());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("cancel.error"));
        }
    }

    public Page<Reviews> findAllByStatus(Enums.ReviewStatus status, Pageable pageable) {
        return reviewRepository.findAllByStatus(status, pageable);
    }

    public Optional<Reviews> findByIdAndStatus(long id, Enums.ReviewStatus status) {
        return reviewRepository.findByIdAndStatus(id, status);
    }

    private void getAttributeReviews(Reviews reviews, ReviewDto reviewDto) {
        Optional<User> optionalUser = userService.findById(reviewDto.getUser().getId());
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("account.not.found"));
        }

        Optional<Flights> optionalFlight = flightService.findById(reviewDto.getFlights().getId());
        if (!optionalFlight.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("flights.not.found"));
        }

        reviews.setUser(optionalUser.get());
        reviews.setFlights(optionalFlight.get());
    }
}
