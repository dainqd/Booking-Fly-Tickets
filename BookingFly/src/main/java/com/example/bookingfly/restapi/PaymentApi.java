package com.example.bookingfly.restapi;

import com.example.bookingfly.dto.PaymentDto;
import com.example.bookingfly.entity.Payments;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.service.MessageResourceService;
import com.example.bookingfly.service.PaymentService;
import com.example.bookingfly.service.UserService;
import com.example.bookingfly.util.Enums;
import com.example.bookingfly.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/payments/")
@RequiredArgsConstructor
public class PaymentApi {
    final PaymentService paymentService;
    final MessageResourceService messageResourceService;
    final UserService userService;

    @GetMapping("list")
    public Page<PaymentDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return paymentService.findAllByStatus(Enums.PaymentStatus.PAID, pageable).map(PaymentDto::new);
    }

    @GetMapping("{id}")
    public PaymentDto getDetail(@PathVariable("id") Long id) {
        Optional<Payments> optionalPayments = paymentService.findByIdAndStatus(id, Enums.PaymentStatus.PAID);
        if (!optionalPayments.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("payment.not.found"));
        }
        return new PaymentDto(optionalPayments.get());
    }

    @PostMapping("create")
    public PaymentDto create(@RequestBody PaymentDto paymentDto) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("account.not.found"));
        }
        User user = optionalUser.get();
        return new PaymentDto(paymentService.create(paymentDto, user.getId()));
    }
}
