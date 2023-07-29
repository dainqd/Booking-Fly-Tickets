package com.example.bookingfly.dto;

import com.example.bookingfly.entity.Airports;
import com.example.bookingfly.entity.Location;
import com.example.bookingfly.util.Enums;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirportDto {
    private long id;
    private String name;
    private Location location;
    private Enums.AirportStatus status = Enums.AirportStatus.ACTIVE;

    public AirportDto(Airports airports) {
        BeanUtils.copyProperties(airports, this);
    }
}
