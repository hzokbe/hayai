package com.hzokbe.hayai.auth.mapper.user;

import com.hzokbe.hayai.auth.dto.user.SignUpRequestDTO;
import com.hzokbe.hayai.auth.entity.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserEntityMapper {
    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "passwordHash", expression = "java(passwordEncoder.encode(dto.password()))")
    public abstract UserEntity toEntity(SignUpRequestDTO dto);
}
