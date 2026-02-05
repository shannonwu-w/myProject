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

    @Query("SELECT r FROM Reservations r WHERE " +
            "LOWER(r.name) LIKE LOWER(:pattern) OR " +
            "LOWER(r.email) LIKE LOWER(:pattern) OR " +
            "r.phone LIKE :pattern OR " +
            "r.message LIKE :pattern OR " +
            "CAST(r.resvDate as string) LIKE :pattern OR " + // 💡 轉型日期
            "CAST(r.people as string) LIKE :pattern OR " +   // 💡 轉型數字
            "CAST(r.reservationId as string) LIKE :pattern " +
            "ORDER BY r.resvDate DESC")
    Page<Reservations> findByAllFields(@Param("pattern") String pattern, Pageable pageable);

    Page<Reservations> findAll(Pageable pageable);
}
