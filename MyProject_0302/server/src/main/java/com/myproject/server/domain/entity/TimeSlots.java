package com.myproject.server.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
