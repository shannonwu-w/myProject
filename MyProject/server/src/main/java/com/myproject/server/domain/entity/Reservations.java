package com.myproject.server.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RESERVATIONS")
public class Reservations {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "RESERVATION_ID_SEQ")
    @SequenceGenerator(sequenceName = "RESERVATION_ID_SEQ",allocationSize = 1)
    @Column(name = "RESERVATION_ID")
    private Long reservationId;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "PEOPLE", nullable = false)
    private Integer people; // 對應 NUMBER(2,0)

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "PHONE", nullable = false, length = 20)
    private String phone;

    @Column(name = "RESV_DATE", nullable = false, length = 10)
    private String resvDate;

    @Column(name = "TIME_SLOT", nullable = false)
    private String timeSlot;

    @Column(name = "MESSAGE", length = 200)
    private String message;

    @ManyToOne
    @JoinColumn(name = "TABLE_ID", nullable = false)
    private TableList tableList; // 對應 TABLE_ID NUMBER

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private Users users; // 對應 USER_ID NUMBER

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}