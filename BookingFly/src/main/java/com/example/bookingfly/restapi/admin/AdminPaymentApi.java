package com.example.bookingfly.restapi.admin;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/admin/api/payments/")
@RequiredArgsConstructor
public class AdminPaymentApi {
    final PaymentService paymentService;
    final MessageResourceService messageResourceService;
    final UserService userService;

    @GetMapping("")
    public Page<PaymentDto> getList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                    @RequestParam(value = "status", required = false, defaultValue = "") Enums.PaymentStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (status != null) {
            return paymentService.findAllByStatus(status, pageable).map(PaymentDto::new);
        }
        return paymentService.findAll(pageable).map(PaymentDto::new);
    }

    @GetMapping("{id}/{status}")
    public PaymentDto getDetail(@PathVariable(name = "id") Long id, @PathVariable(name = "status") Enums.PaymentStatus status) {
        Optional<Payments> optionalPayments;
        if (status != null) {
            optionalPayments = paymentService.findByIdAndStatus(id, status);
        } else {
            optionalPayments = paymentService.findById(id);
        }
        if (!optionalPayments.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("payment.not.found"));
        }
        return new PaymentDto(optionalPayments.get());
    }

    @PutMapping("/{id}/{status}")
    public ResponseEntity<String> toggle(@PathVariable("id") long id, @PathVariable("status") Enums.PaymentStatus status) {
        String username = Utils.getUsername();
        Optional<User> optionalUser = userService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        paymentService.changeStatus(id, status, user.getId());
        return new ResponseEntity<>(messageResourceService.getMessage("update.success"), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        String username = Utils.getUsername();
        System.out.println(username);
        Optional<User> optionalUser = userService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    messageResourceService.getMessage("id.not.found"));
        }
        User user = optionalUser.get();
        paymentService.deleteById(id, user.getId());
        return new ResponseEntity<>(messageResourceService.getMessage("delete.success"), HttpStatus.OK);
    }
}
