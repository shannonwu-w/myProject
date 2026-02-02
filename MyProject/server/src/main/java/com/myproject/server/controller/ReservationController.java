package com.myproject.server.controller;

import com.myproject.server.domain.dto.ReservationsDto;
import com.myproject.server.domain.dto.UserCert;
import com.myproject.server.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/make")
    public ResponseEntity<?> createReservation(@RequestBody ReservationsDto dto, HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");

        if (userCert == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "請先登入"));
        }

        try {
            reservationService.makeReservation(dto, userCert);
            return ResponseEntity.ok(Map.of("message", "訂位成功！"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/history")
    public List<ReservationsDto> myReservations(@RequestParam Long userId) {
        return reservationService.getMyReservations(userId);


    }

    }

