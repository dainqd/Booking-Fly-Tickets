package com.example.bookingfly.dto;

import com.example.bookingfly.entity.Airlines;
import com.example.bookingfly.entity.Airports;
import com.example.bookingfly.entity.Flights;
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
public class FlightDto {
    private long id;
    private Airlines airlines;
    private Airports from_airport_id;
    private Airports to_airport_id;
    private LocalDateTime departure_time;
    private LocalDateTime arrival_time;
    private int duration;
    private float price;
    private int available_seats;
    private Enums.FlightStatus status = Enums.FlightStatus.ACTIVE;

    public FlightDto(Flights flight) {
        BeanUtils.copyProperties(flight, this);
    }
}
