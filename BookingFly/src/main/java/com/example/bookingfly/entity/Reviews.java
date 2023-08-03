package com.example.bookingfly.entity;

import com.example.bookingfly.dto.ReviewDto;
import com.example.bookingfly.entity.basic.BasicEntity;
import com.example.bookingfly.util.Enums;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Reviews extends BasicEntity {
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
    private Flights flights;
    @Max(5)
    @Min(0)
    private int rating;
    @Lob
    private String comments;
    @Enumerated(EnumType.STRING)
    private Enums.ReviewStatus status = Enums.ReviewStatus.ACTIVE;

    public Reviews(ReviewDto reviewDto) {
        BeanUtils.copyProperties(reviewDto, this);
    }
}
