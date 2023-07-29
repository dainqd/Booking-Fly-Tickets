package com.example.bookingfly.entity;

import com.example.bookingfly.dto.AirportDto;
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
@Table(name = "airports")
public class Airports extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "Airport name is not null")
    private String name;
    @OneToOne
    @JoinColumn(name = "location_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Location location;
    @Enumerated(EnumType.STRING)
    private Enums.AirportStatus status = Enums.AirportStatus.ACTIVE;

    public Airports(AirportDto airportDto) {
        BeanUtils.copyProperties(airportDto, this);
    }
}
