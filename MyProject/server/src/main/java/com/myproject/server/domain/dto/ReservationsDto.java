package com.myproject.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationsDto {
    private String name;
    private Integer people;
    private String email;
    private String phone;
    private String date;
    private String timeSlot;
    private String message;
}