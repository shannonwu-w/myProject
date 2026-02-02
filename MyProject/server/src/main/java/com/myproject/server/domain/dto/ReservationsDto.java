package com.myproject.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationsDto {
    private String name;
    private Integer people;
    private String email;
    private String phone;
    private LocalDate resvDate;
    private String timeSlot;
    private String message;
    private Long userId;
}