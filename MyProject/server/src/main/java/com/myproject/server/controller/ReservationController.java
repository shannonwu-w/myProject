package com.myproject.server.controller;

import com.myproject.server.domain.dto.ReservationsDto;
import com.myproject.server.domain.dto.UserCert;
import com.myproject.server.domain.entity.Reservations;
import com.myproject.server.service.ReservationService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    //新增、修改訂位資料
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

    //使用者查看自己的訂位紀錄
    @GetMapping("/history")
    public List<ReservationsDto> myReservations(@RequestParam Long userId) {
        return reservationService.getMyReservations(userId);
    }

    //刪除訂位
    @PostMapping("/delete/{reservationId}")
    public void deleteReservation(@PathVariable Long reservationId){

       this.reservationService.deleteResv(reservationId);
    }

    //查看全部的訂位
//    @GetMapping("/all-reservations")
//    public List<ReservationsDto> allReservations(){
//        return this.reservationService.allReservations();
//    }

    //得到要編輯的訂位資料
    @GetMapping("/edit/{reservationId}")
    public List<ReservationsDto> editReservation(@PathVariable Long reservationId){
        return this.reservationService.getResvEditData(reservationId);
    }

    @GetMapping("/search")
    public Page<ReservationsDto> searchReservations(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {

        if (keyword == null) {
            return reservationService.getAllReservations(pageable);
        } else {
            return reservationService.getReservations(keyword, pageable);
        }
    }




}

