package com.example.bookingfly.entity;

import com.example.bookingfly.dto.NotificationDto;
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
@Table(name = "notifications")
public class Notifications extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
    @Lob
    private String title;
    @Lob
    private String content;
    @Enumerated(EnumType.STRING)
    private Enums.NotificationStatus status = Enums.NotificationStatus.UNSEEN;

    public Notifications(NotificationDto notificationDto) {
        BeanUtils.copyProperties(notificationDto, this);
    }
}
