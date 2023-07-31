package com.example.bookingfly.dto;

import com.example.bookingfly.entity.Booking;
import com.example.bookingfly.entity.Flights;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.util.Enums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private long id;
    private User user;
    private Flights flight;
    private LocalDateTime booking_time;
    private int num_passengers;
    private float price;
    private float discount_price;
    private float other_price;
    private float total_price;
    private Enums.BookingStatus status = Enums.BookingStatus.PROCESSING;

    public BookingDto(Booking booking) {
        BeanUtils.copyProperties(booking, this);
    }
}
