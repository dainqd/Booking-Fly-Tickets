package com.example.bookingfly.entity;

import com.example.bookingfly.dto.BookingDto;
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
@Table(name = "bookings")
public class Booking extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
    @OneToOne
    @JoinColumn(name = "flight_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Flights flight;
    private LocalDateTime booking_time;
    private int num_passengers;
    private float price;
    private float discount_price;
    private float other_price;
    private float total_price;
    @Enumerated(EnumType.STRING)
    private Enums.BookingStatus status = Enums.BookingStatus.PROCESSING;

    public Booking(BookingDto bookingDto) {
        BeanUtils.copyProperties(bookingDto, this);
    }
}
