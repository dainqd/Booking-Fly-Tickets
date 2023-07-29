package com.example.bookingfly.entity;

import com.example.bookingfly.dto.AirlineDto;
import com.example.bookingfly.entity.basic.BasicEntity;
import com.example.bookingfly.util.Enums;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airlines")
public class Airlines extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "Airline name is not null")
    private String name;
    @Enumerated(EnumType.STRING)
    private Enums.AirlineArea area = Enums.AirlineArea.INLAND;
    @Enumerated(EnumType.STRING)
    private Enums.AirlineStatus status = Enums.AirlineStatus.ACTIVE;
    public Airlines(AirlineDto airlineDto) {
        BeanUtils.copyProperties(airlineDto, this);
    }
}
