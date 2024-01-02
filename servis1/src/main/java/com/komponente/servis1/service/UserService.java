package com.komponente.servis1.service;

import com.komponente.servis1.dto.TokenRequestDto;
import com.komponente.servis1.dto.TokenResponseDto;
import com.komponente.servis1.dto.UserCreateDto;
import com.komponente.servis1.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserDto> findAll(Pageable pageable);

    UserDto add(UserCreateDto userCreateDto);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);
    UserDto updateUser(Long userId, UserDto userDto);

}
