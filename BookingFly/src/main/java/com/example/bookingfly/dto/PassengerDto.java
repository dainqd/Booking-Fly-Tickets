package com.example.bookingfly.dto;

import com.example.bookingfly.entity.Booking;
import com.example.bookingfly.entity.Passengers;
import com.example.bookingfly.util.Enums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDto {
    private long id;
    private Booking booking;
    private String name;
    private int age;
    private Enums.Gender gender = Enums.Gender.OTHER;
    private Enums.PassengerStatus status = Enums.PassengerStatus.ACTIVE;

    public PassengerDto(Passengers passengers) {
        BeanUtils.copyProperties(passengers, this);
    }
}
