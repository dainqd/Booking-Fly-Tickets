package com.example.bookingfly.dto;

import com.example.bookingfly.entity.Notifications;
import com.example.bookingfly.entity.User;
import com.example.bookingfly.util.Enums;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private long id;
    private User user;
    private String title;
    private String content;
    private Enums.NotificationStatus status = Enums.NotificationStatus.UNSEEN;

    public NotificationDto(Notifications notifications) {
        BeanUtils.copyProperties(notifications, this);
    }
}
