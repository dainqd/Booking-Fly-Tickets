package com.example.bookingfly.entity;

import com.example.bookingfly.dto.PaymentDto;
import com.example.bookingfly.entity.basic.BasicEntity;
import com.example.bookingfly.util.Enums;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payments extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "booking_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Booking booking;
    @Enumerated(EnumType.STRING)
    private Enums.PaymentMethod method = Enums.PaymentMethod.IMMEDIATE;
    @Enumerated(EnumType.STRING)
    private Enums.PaymentStatus status = Enums.PaymentStatus.UNPAID;
    private float total_amount;

    public Payments(PaymentDto paymentDto) {
        BeanUtils.copyProperties(paymentDto, this);
    }
}
