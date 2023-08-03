package com.example.bookingfly.restapi;

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
@RequestMapping("/admin/api/reviews/")
@RequiredArgsConstructor
public class ReviewApi {
    final ReviewService reviewService;
    final MessageResourceService messageResourceService;
    final UserService userService;


    @GetMapping("list")
    public Page<ReviewDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                   @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return reviewService.findAllByStatus(Enums.ReviewStatus.ACTIVE, pageable).map(ReviewDto::new);
    }

    @GetMapping("{id}")
    public ReviewDto getDetail(@PathVariable("id") Long id) {
        Optional<Reviews> optionalAirports = reviewService.findByIdAndStatus(id, Enums.ReviewStatus.ACTIVE);
        if (!optionalAirports.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("airports.not.found"));
        }
        return new ReviewDto(optionalAirports.get());
    }

    @PostMapping("create")
    public ReviewDto create(@RequestBody ReviewDto reviewDto) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("account.not.found"));
        }
        User user = optionalUser.get();
        return new ReviewDto(reviewService.create(reviewDto, user.getId()));
    }
}
