package com.komponente.servis1.service.impl;

import com.komponente.servis1.dto.TokenRequestDto;
import com.komponente.servis1.dto.TokenResponseDto;
import com.komponente.servis1.dto.UserCreateDto;
import com.komponente.servis1.dto.UserDto;
import com.komponente.servis1.repository.ClientUserRepository;
import com.komponente.servis1.service.UserService;
import com.komponente.servis1.domain.AdminUser;
import com.komponente.servis1.domain.ClientUser;
import com.komponente.servis1.domain.ManagerUser;
import com.komponente.servis1.exception.NotFoundException;
import com.komponente.servis1.repository.AdminUserRepository;
import com.komponente.servis1.repository.ManagerUserRepository;
import com.komponente.servis1.secutiry.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    ClientUserRepository clientUserRepository;
    ManagerUserRepository managerUserRepository;
    AdminUserRepository adminUserRepository;
    TokenService tokenService;

    public UserServiceImpl(ClientUserRepository clientUserRepository, ManagerUserRepository managerUserRepository, AdminUserRepository adminUserRepository, TokenService tokenService) {
        this.clientUserRepository = clientUserRepository;
        this.managerUserRepository = managerUserRepository;
        this.adminUserRepository = adminUserRepository;
        this.tokenService = tokenService;
    }
    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public UserDto add(UserCreateDto userCreateDto) {
        return null;
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        Optional<ClientUser> clientUser = clientUserRepository
                .findClientUserByEmailAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword());

        if (clientUser.isPresent()) {
            ClientUser user = clientUser.get();
            if (!user.isActive()) {
                throw new IllegalStateException("User account is disabled.");
            }
            if (!user.getVerified().equals("verified")) {
                throw new IllegalStateException("User account is not verified.");
            }
            Claims claims = Jwts.claims();
            claims.put("id", user.getId());
            claims.put("role", user.getRole().getName());
            String token = tokenService.generate(claims);
            return new TokenResponseDto(token);
        }

        Optional<ManagerUser> managerUser = managerUserRepository
                .findManagerUserByEmailAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword());

        if (managerUser.isPresent()) {
            ManagerUser user = managerUser.get();
            if (!user.isActive()) {
                throw new IllegalStateException("Manager account is disabled.");
            }
            if (!user.getVerified().equals("verified")) {
                throw new IllegalStateException("Manager account is not verified.");
            }
            Claims claims = Jwts.claims();
            claims.put("id", user.getId());
            claims.put("role", user.getRole().getName());
            String token = tokenService.generate(claims);
            return new TokenResponseDto(token);
        }

        Optional<AdminUser> adminUser = adminUserRepository
                .findAdminUserByEmailAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword());

        if (adminUser.isPresent()) {
            AdminUser user = adminUser.get();
            Claims claims = Jwts.claims();
            claims.put("id", user.getId());
            claims.put("role", user.getRole().getName());
            String token = tokenService.generate(claims);
            return new TokenResponseDto(token);
        }

        throw new NotFoundException("User not found.");
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        return null;
    }
}
