package com.myproject.server.mapper;

import com.myproject.server.domain.dto.UsersDto;
import com.myproject.server.domain.entity.Users;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

    @Autowired
    private ModelMapper modelMapper;

    public UsersDto toDto(Users users) {
        // Entity 轉 DTO
        return modelMapper.map(users, UsersDto.class);
    }

    public Users toEntity(UsersDto usersDto) {
        // DTO 轉 Entity
        return modelMapper.map(usersDto, Users.class);
    }

    // 將 DTO 的值 覆蓋到 現有的 Entity 上
    public void updateEntityFromDto(UsersDto dto, Users entity) {

        modelMapper.map(dto, entity);

    }

}
