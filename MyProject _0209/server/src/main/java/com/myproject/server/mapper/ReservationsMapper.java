package com.myproject.server.mapper;

import com.myproject.server.domain.dto.ReservationsDto;
import com.myproject.server.domain.entity.Reservations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservationsMapper {
    @Autowired
    private ModelMapper modelMapper;
    public ReservationsDto toDto(Reservations reservations){

        return modelMapper.map(reservations, ReservationsDto.class);

    }
    public Reservations toEntity(ReservationsDto reservationsDto){

        return  modelMapper.map(reservationsDto,Reservations.class);

    }

}
