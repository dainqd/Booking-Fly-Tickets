package com.example.bookingfly.dto;

import com.example.bookingfly.entity.Flights;
import com.example.bookingfly.entity.Reviews;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.util.Enums;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private long id;
    private User user;
    private Flights flights;
    private int rating;
    private String comments;
    private Enums.ReviewStatus status = Enums.ReviewStatus.ACTIVE;

    public ReviewDto(Reviews reviews) {
        BeanUtils.copyProperties(reviews, this);
    }
}
