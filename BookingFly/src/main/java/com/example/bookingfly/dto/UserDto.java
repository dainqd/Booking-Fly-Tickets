package com.example.bookingfly.dto;

import com.example.bookingfly.entity.Flights;
import com.example.bookingfly.entity.Role;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.util.Enums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    private String avt;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phoneNumber;
    private LocalDateTime birthday;
    private String gender;
    private String address;
    private String verifyCode = "";
    private String referralCode = "";
    private boolean verified = false;
    private String password;
    private Set<Role> roles = new HashSet<>();
    private Enums.AccountStatus status;

    public UserDto(User user) {
        BeanUtils.copyProperties(user, this);
    }
}
