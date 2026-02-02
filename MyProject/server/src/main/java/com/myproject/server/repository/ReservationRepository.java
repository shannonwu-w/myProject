package com.myproject.server.repository;

import com.myproject.server.domain.entity.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservations, Long> {
    List<Reservations> findByResvDateAndTimeSlot(LocalDate resvDate, String timeSlot);
    List<Reservations> findByUserId(Long userId);
}
