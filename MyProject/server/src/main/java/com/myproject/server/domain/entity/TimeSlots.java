package com.myproject.server.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TIME_SLOTS")
public class TimeSlots {

    @Id
    @Column(name = "TIME_ID")
    private Long timeId;

    @Column(name = "TIME_SLOT")
    private String timeSlot;


}
