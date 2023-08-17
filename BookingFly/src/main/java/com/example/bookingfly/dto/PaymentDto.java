package com.example.bookingfly.dto;

import com.example.bookingfly.entity.Booking;
import com.example.bookingfly.entity.Payments;
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
public class PaymentDto {
    private long id;
    private Booking booking;
    private Enums.PaymentMethod method = Enums.PaymentMethod.IMMEDIATE;
    private Enums.PaymentStatus status = Enums.PaymentStatus.UNPAID;
    private float total_amount;

    public PaymentDto(Payments payments) {
        BeanUtils.copyProperties(payments, this);
    }
}
