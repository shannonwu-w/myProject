package com.myproject.server.repository;

import com.myproject.server.domain.dto.ReservationsDto;
import com.myproject.server.domain.entity.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservations, Long> {
    List<Reservations> findByResvDateAndTimeSlot(LocalDate resvDate, String timeSlot);
    List<Reservations> findByUserId(Long userId);
    List<Reservations> findByReservationId(Long reservationId);

    @Query(value = "SELECT * FROM reservations WHERE " +
            "LOWER(name) LIKE LOWER(:pattern) OR " +
            "LOWER(email) LIKE LOWER(:pattern) OR " +
            "phone LIKE :pattern " +
            "ORDER BY resv_date DESC",
            nativeQuery = true) // 👈 必須加上這行
    List<Reservations> findByMultipleFields(@Param("pattern") String pattern);

}
