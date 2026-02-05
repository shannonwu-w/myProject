package com.myproject.server.repository;

import com.myproject.server.domain.dto.ReservationsDto;
import com.myproject.server.domain.entity.Reservations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservations, Long> {
    List<Reservations> findByResvDateAndTimeSlot(LocalDate resvDate, String timeSlot);
    List<Reservations> findByUserId(Long userId);
    List<Reservations> findByReservationId(Long reservationId);

    @Query(value = "SELECT * FROM reservations r WHERE " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :pattern, '%')) OR " +
            "LOWER(r.email) LIKE LOWER(CONCAT('%', :pattern, '%')) OR " +
            "r.phone LIKE CONCAT('%', :pattern, '%') OR " +
            "r.message LIKE CONCAT('%', :pattern, '%') OR " +
            "CAST(r.resv_date AS CHAR) LIKE CONCAT('%', :pattern, '%') OR " +
            "CAST(r.people AS CHAR) LIKE CONCAT('%', :pattern, '%') OR " +
            "CAST(r.reservation_id AS CHAR) LIKE CONCAT('%', :pattern, '%') " +
            "ORDER BY r.resv_date DESC",
            countQuery = "SELECT count(*) FROM reservations r WHERE " +
                    "LOWER(r.name) LIKE LOWER(CONCAT('%', :pattern, '%')) OR " +
                    "LOWER(r.email) LIKE LOWER(CONCAT('%', :pattern, '%'))",
            nativeQuery = true)
    Page<Reservations> findByAllFields(@Param("pattern") String pattern, Pageable pageable);

    Page<Reservations> findAll(Pageable pageable);
}
