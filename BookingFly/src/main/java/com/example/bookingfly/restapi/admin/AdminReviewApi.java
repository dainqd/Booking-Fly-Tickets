package com.example.bookingfly.restapi.admin;

import com.example.bookingfly.dto.FlightDto;
import com.example.bookingfly.dto.ReviewDto;
import com.example.bookingfly.entity.Reviews;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.service.MessageResourceService;
import com.example.bookingfly.service.ReviewService;
import com.example.bookingfly.service.UserService;
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
@RequestMapping("/api/reviews/")
@RequiredArgsConstructor
public class AdminReviewApi {
    final ReviewService reviewService;
    final MessageResourceService messageResourceService;
    final UserService userService;

    @GetMapping("")
    public Page<ReviewDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                   @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                   @RequestParam(value = "status", required = false, defaultValue = "") Enums.ReviewStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (status != null) {
            return reviewService.findAllByStatus(status, pageable).map(ReviewDto::new);
        }
        return reviewService.findAll(pageable).map(ReviewDto::new);
    }

    @GetMapping("{id}/{status}")
    public ReviewDto getDetail(@PathVariable(name = "id") Long id, @PathVariable(name = "status") Enums.ReviewStatus status) {
        Optional<Reviews> optionalReviews;
        if (status != null) {
            optionalReviews = reviewService.findByIdAndStatus(id, status);
        } else {
            optionalReviews = reviewService.findById(id);
        }
        if (!optionalReviews.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("flights.not.found"));
        }
        return new ReviewDto(optionalReviews.get());
    }

    @PutMapping("/{id}/{status}")
    public ResponseEntity<String> toggle(@PathVariable("id") long id, @PathVariable("status") Enums.ReviewStatus status) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        reviewService.toggle(id, status, user.getId());
        return new ResponseEntity<>(messageResourceService.getMessage("update.success"), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        String username = Utils.getUsername();
        System.out.println(username);
        Optional<User> optionalUser = userService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        reviewService.deleteById(id, user.getId());
        return new ResponseEntity<>(messageResourceService.getMessage("delete.success"), HttpStatus.OK);
    }
}
