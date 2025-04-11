package com.example.testtaskeffectmobile.mapper;

import com.example.testtaskeffectmobile.dto.request.RegisterUserRequestDto;
import com.example.testtaskeffectmobile.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toNewEntity(RegisterUserRequestDto registerUserRequestDto);
}
