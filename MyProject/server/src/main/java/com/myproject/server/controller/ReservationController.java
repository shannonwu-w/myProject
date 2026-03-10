package com.myproject.server.controller;

import com.myproject.server.domain.dto.ReservationsDto;
import com.myproject.server.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // 新增、修改訂位資料
    @PostMapping("/make")
    public ResponseEntity<?> createReservation(@RequestBody ReservationsDto dto) {
        // 從 Spring Security context 取得使用者 email (JWT Filter 已放入 principal)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "使用者未登入"));
        }
        String email = auth.getPrincipal().toString();

        try {
            reservationService.makeReservationByEmail(dto, email);
            return ResponseEntity.ok(Map.of("message", "訂位成功！"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // 使用者查看自己的訂位紀錄
    @GetMapping("/history")
    public ResponseEntity<?> myReservations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "使用者未登入"));
        }
        String email = auth.getPrincipal().toString();
        try {
            List<ReservationsDto> reservations = reservationService.getMyReservationsByEmail(email);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // 刪除訂位
    @PostMapping("/delete/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long reservationId) {
        try {
            reservationService.deleteResv(reservationId);
            return ResponseEntity.ok(Map.of("message", "刪除成功"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // 得到要編輯的訂位資料
    @GetMapping("/edit/{reservationId}")
    public ResponseEntity<?> editReservation(@PathVariable Long reservationId) {
        try {
            List<ReservationsDto> dto = reservationService.getResvEditData(reservationId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // 查詢全部或關鍵字
    @GetMapping("/search")
    public ResponseEntity<?> searchReservations(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        try {
            Page<ReservationsDto> page;
            if (keyword == null || keyword.isBlank()) {
                page = reservationService.getAllReservations(pageable);
            } else {
                page = reservationService.getReservations(keyword, pageable);
            }
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}