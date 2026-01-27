package com.myproject.server.repository;

import com.myproject.server.domain.entity.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservations, Long> {
    List<Reservations> findByResvDateAndTimeSlot(String resvDate, String timeSlot);

}
