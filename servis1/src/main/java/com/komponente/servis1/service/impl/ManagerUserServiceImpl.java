package com.komponente.servis1.service.impl;

import com.komponente.servis1.domain.EmailType;
import com.komponente.servis1.dto.*;
import com.komponente.servis1.exception.NotFoundException;
import com.komponente.servis1.repository.EmailTypeRepository;
import com.komponente.servis1.repository.ManagerUserRepository;
import com.komponente.servis1.secutiry.service.TokenService;
import com.komponente.servis1.service.UserService;
import com.komponente.servis1.domain.ManagerUser;
import com.komponente.servis1.listener.helper.MessageHelper;
import com.komponente.servis1.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ManagerUserServiceImpl implements UserService {

    private TokenService tokenService;
    private ManagerUserRepository gymManagerUserRepository;
    private UserMapper userMapper;
    @Autowired
    EmailTypeRepository emailTypeRepository;
    String destinationEmail;
    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    MessageHelper messageHelper;

    public ManagerUserServiceImpl(ManagerUserRepository gymManagerUserRepository, UserMapper userMapper, @Value("${destination.email}") String destinationEmail) {
        this.gymManagerUserRepository = gymManagerUserRepository;
        this.userMapper = userMapper;
        this.destinationEmail = destinationEmail;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return gymManagerUserRepository.findAll(pageable)
                .map(userMapper::userToUserDto);
    }

    @Override
    public UserDto add(UserCreateDto userCreateDto) {
        ManagerUser gymManagerUser = (ManagerUser) userMapper.userCreateDtoToUser(userCreateDto);
        gymManagerUserRepository.save(gymManagerUser);
        System.out.println("User added: " + gymManagerUser);
        System.out.println(gymManagerUser.getVerified());

        Optional<EmailType> emailType = emailTypeRepository.findEmailTypeByType("ACTIVATION");
        if (!emailType.isPresent()) {
            throw new NotFoundException("Email type not found.");
        }
        EmailType emailType1 = emailType.get();

        EmailDto emailDto = userMapper.createEmailDtoToUser(userCreateDto, emailType1, gymManagerUser.getVerified());
        emailDto.setRole(gymManagerUser.getRole().getName());
        emailDto.setUserId(gymManagerUser.getId().toString());

        jmsTemplate.convertAndSend(destinationEmail, messageHelper.createTextMessage(emailDto));

        System.out.println(gymManagerUser.getRole().getName());
        return userMapper.userToUserDto(gymManagerUser);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        ManagerUser gymManagerUser = gymManagerUserRepository
                .findManagerUserByEmailAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format("Manager with username: %s not found.", tokenRequestDto.getUsername())));

        if (!gymManagerUser.isActive()) {
            throw new IllegalStateException("Manager account is disabled.");
        }

        System.out.println(gymManagerUser.getVerified());
        if (!gymManagerUser.getVerified().equals("verified")) {
            throw new IllegalStateException("Manager account is not verified.");
        }

        Claims claims = Jwts.claims();
        claims.put("id", gymManagerUser.getId());
        claims.put("role", gymManagerUser.getRole().getName());
        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        ManagerUser gymManagerUser = gymManagerUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Manager not found."));

        gymManagerUser.setEmail(userDto.getEmail());
        gymManagerUser.setFirstName(userDto.getFirstName());
        gymManagerUser.setLastName(userDto.getLastName());

        gymManagerUserRepository.save(gymManagerUser);
        return userMapper.userToUserDto(gymManagerUser);
    }

    public String updateVerficationToken(String token) {
        ManagerUser clientUser = gymManagerUserRepository.findManagerUserByVerified(token)
                .orElseThrow(() -> new NotFoundException("Manager not found."));

        clientUser.setVerified("verified");

        gymManagerUserRepository.save(clientUser);
        return "User verified";
    }

    public UserDto findById(Long id) {
        ManagerUser gymManagerUser = gymManagerUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Manager not found."));
        return userMapper.userToUserDto(gymManagerUser);
    }
}

