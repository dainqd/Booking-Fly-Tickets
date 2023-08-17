package com.example.bookingfly.service;

import com.example.bookingfly.dto.PaymentDto;
import com.example.bookingfly.dto.TestBankTransferDto;
import com.example.bookingfly.entity.Booking;
import com.example.bookingfly.entity.Payments;
import com.example.bookingfly.repository.BookingRepository;
import com.example.bookingfly.repository.PaymentRepository;
import com.example.bookingfly.util.Enums;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    final PaymentRepository paymentRepository;
    final BookingRepository bookingRepository;
    final MessageResourceService messageResourceService;
    final UserService userService;

    public Page<Payments> findAll(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    public Optional<Payments> findById(long id) {
        return paymentRepository.findById(id);
    }

    public Payments save(Payments payments) {
        return paymentRepository.save(payments);
    }

    public Payments create(PaymentDto paymentDto, long adminId) {
        try {
            Payments payments = new Payments();

            BeanUtils.copyProperties(paymentDto, payments);
            payments.setCreatedAt(LocalDateTime.now());
            payments.setCreatedBy(adminId);
            getAttributePayments(payments, paymentDto);
            return paymentRepository.save(payments);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("create.error"));

        }
    }

//    public Payments checkout(PaymentDto paymentDto, TestBankTransferDto testBankTransferDto, long adminId) {
//        try {
//
//            Optional<Payments> optionalPayments = paymentRepository.findById(paymentDto.getId());
//            if (!optionalPayments.isPresent()) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//                        messageResourceService.getMessage("payment.not.found"));
//            }
//            Payments payments = optionalPayments.get();
//
//            return paymentRepository.save(payments);
//        } catch (Exception exception) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//                    messageResourceService.getMessage("create.error"));
//
//        }
//    }

    public Payments update(PaymentDto paymentDto, long adminID) {
        try {
            Optional<Payments> optionalPayments = paymentRepository.findById(paymentDto.getId());
            if (!optionalPayments.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("payment.not.found"));
            }
            Payments payments = optionalPayments.get();

            BeanUtils.copyProperties(paymentDto, payments);
            payments.setUpdatedAt(LocalDateTime.now());
            payments.setUpdatedBy(adminID);

            getAttributePayments(payments, paymentDto);

            return paymentRepository.save(payments);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("update.error"));
        }
    }

    public void deleteById(long id, long adminID) {
        try {
            Optional<Payments> optionalPayments = paymentRepository.findById(id);
            if (!optionalPayments.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("payment.not.found"));
            }
            optionalPayments.get().setStatus(Enums.PaymentStatus.DELETED);
            optionalPayments.get().setDeletedAt(LocalDateTime.now());
            optionalPayments.get().setDeletedBy(adminID);
            paymentRepository.save(optionalPayments.get());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("delete.error"));
        }
    }

    public void changeStatus(long id, Enums.PaymentStatus status, long adminID) {
        try {
            Optional<Payments> optionalFlights = paymentRepository.findById(id);
            if (!optionalFlights.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        messageResourceService.getMessage("payment.not.found"));
            }
            if (status != null) {
                optionalFlights.get().setStatus(status);
            } else {
                optionalFlights.get().setStatus(Enums.PaymentStatus.UNPAID);
            }
            optionalFlights.get().setDeletedAt(LocalDateTime.now());
            optionalFlights.get().setDeletedBy(adminID);
            paymentRepository.save(optionalFlights.get());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("delete.error"));
        }
    }

    public Page<Payments> findAllByStatus(Enums.PaymentStatus status, Pageable pageable) {
        return paymentRepository.findAllByStatus(status, pageable);
    }

    public Optional<Payments> findByIdAndStatus(long id, Enums.PaymentStatus status) {
        return paymentRepository.findByIdAndStatus(id, status);
    }

    private void getAttributePayments(Payments payments, PaymentDto paymentDto) {
        Optional<Booking> optionalBooking = bookingRepository.findById(paymentDto.getBooking().getId());
        if (!optionalBooking.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    messageResourceService.getMessage("booking.not.found"));
        }

        payments.setBooking(optionalBooking.get());
    }
}
