package com.example.testtaskeffectmobile.mapper;

import com.example.testtaskeffectmobile.dto.RegisterUserDto;
import com.example.testtaskeffectmobile.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toNewEntity(RegisterUserDto registerUserDto);
}
