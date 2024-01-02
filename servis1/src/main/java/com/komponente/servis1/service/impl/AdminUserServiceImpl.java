package com.komponente.servis1.service.impl;

import com.komponente.servis1.dto.TokenRequestDto;
import com.komponente.servis1.service.UserService;
import com.komponente.servis1.domain.AdminUser;
import com.komponente.servis1.domain.ClientUser;
import com.komponente.servis1.dto.TokenResponseDto;
import com.komponente.servis1.dto.UserCreateDto;
import com.komponente.servis1.dto.UserDto;
import com.komponente.servis1.exception.NotFoundException;
import com.komponente.servis1.mapper.UserMapper;
import com.komponente.servis1.repository.AdminUserRepository;
import com.komponente.servis1.repository.ClientUserRepository;
import com.komponente.servis1.repository.ManagerUserRepository;
import com.komponente.servis1.secutiry.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminUserServiceImpl implements UserService {
    private TokenService tokenService;
    private AdminUserRepository adminUserRepository;
    private ClientUserRepository clientUserRepository;
    private ManagerUserRepository managerUserRepository;
    private UserMapper userMapper;

    public AdminUserServiceImpl(TokenService tokenService, AdminUserRepository adminUserRepository, UserMapper userMapper, ClientUserRepository clientUserRepository, ManagerUserRepository managerUserRepository) {
        this.tokenService = tokenService;
        this.adminUserRepository = adminUserRepository;
        this.userMapper = userMapper;
        this.clientUserRepository = clientUserRepository;
        this.managerUserRepository = managerUserRepository;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return adminUserRepository.findAll(pageable)
                .map(userMapper::userToUserDto);
    }

    @Override
    public UserDto add(UserCreateDto userCreateDto) {
        AdminUser adminUser = (AdminUser) userMapper.userCreateDtoToUser(userCreateDto);
        adminUserRepository.save(adminUser);
        return userMapper.userToUserDto(adminUser);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        AdminUser adminUser = adminUserRepository
                .findAdminUserByEmailAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s not found.", tokenRequestDto.getUsername())));

        if (!adminUser.isActive()) {
            throw new IllegalStateException("User account is disabled.");
        }

        Claims claims = Jwts.claims();
        claims.put("id", adminUser.getId());
        claims.put("role", adminUser.getRole().getName());
        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        AdminUser adminUser = adminUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));

        adminUser.setEmail(userDto.getEmail());
        adminUser.setFirstName(userDto.getFirstName());
        adminUser.setLastName(userDto.getLastName());

        adminUserRepository.save(adminUser);
        return userMapper.userToUserDto(adminUser);
    }
    public void enableUser(Long userId) {
        ClientUser user = clientUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));
        user.setActive(true);
        clientUserRepository.save(user);
    }

    public void disableUser(Long userId) {
        ClientUser user = clientUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));
        user.setActive(false);
        clientUserRepository.save(user);
    }
}
