package com.example.bookingfly.dto;

import com.example.bookingfly.entity.Airlines;
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
public class AirlineDto {
    private long id;
    private String name;
    private Enums.AirlineArea area = Enums.AirlineArea.INLAND;
    private Enums.AirlineStatus status = Enums.AirlineStatus.ACTIVE;

    public AirlineDto(Airlines airlines) {
        BeanUtils.copyProperties(airlines, this);
    }
}
