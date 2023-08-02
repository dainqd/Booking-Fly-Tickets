package com.example.bookingfly.entity;

import com.example.bookingfly.dto.PassengerDto;
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
@Table(name = "passengers")
public class Passengers extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "booking_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Booking booking;
    @NotNull(message = "Passengers name is not null")
    private String name;
    private int age;
    @Enumerated(EnumType.STRING)
    private Enums.Gender gender = Enums.Gender.OTHER;
    @Enumerated(EnumType.STRING)
    private Enums.PassengerStatus status = Enums.PassengerStatus.ACTIVE;

    public Passengers(PassengerDto passengerDto) {
        BeanUtils.copyProperties(passengerDto, this);
    }
}
