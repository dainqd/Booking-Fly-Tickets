package com.example.bookingfly.entity;

import com.example.bookingfly.dto.FlightDto;
import com.example.bookingfly.entity.basic.BasicEntity;
import com.example.bookingfly.util.Enums;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flights")
public class Flights extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "airline_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Airlines airlines;
    // Sân bay đi
    @OneToOne
    @JoinColumn(name = "from_airport_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Airports from_airport_id;
    // Sân bay đến
    @OneToOne
    @JoinColumn(name = "to_airport_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Airports to_airport_id;
    // Thoi gian khoi hanh
    private LocalDateTime departure_time;
    // Thoi gian ket thuc
    private LocalDateTime arrival_time;
    // Thoi gian bay
    private int duration;
    private float price;
    // So luong ve con trong
    private int available_seats;
    @Enumerated(EnumType.STRING)
    private Enums.FlightStatus status = Enums.FlightStatus.ACTIVE;

    public Flights(FlightDto flightDto) {
        BeanUtils.copyProperties(flightDto, this);
    }
}
