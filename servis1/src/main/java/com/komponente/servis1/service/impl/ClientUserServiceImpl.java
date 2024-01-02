package com.komponente.servis1.service.impl;

import com.komponente.servis1.domain.EmailType;
import com.komponente.servis1.dto.*;
import com.komponente.servis1.repository.ClientUserRepository;
import com.komponente.servis1.repository.EmailTypeRepository;
import com.komponente.servis1.service.UserService;
import com.komponente.servis1.domain.ClientUser;
import com.komponente.servis1.exception.NotFoundException;
import com.komponente.servis1.listener.helper.MessageHelper;
import com.komponente.servis1.mapper.UserMapper;
import com.komponente.servis1.secutiry.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ClientUserServiceImpl implements UserService {

    private ClientUserRepository clientUserRepository;
    private EmailTypeRepository emailTypeRepository;
    private UserMapper userMapper;
    private TokenService tokenService;
    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;
    private String destinationEmail;

    public ClientUserServiceImpl(ClientUserRepository clientUserRepository, UserMapper userMapper, TokenService tokenService, JmsTemplate jmsTemplate, MessageHelper messageHelper, @Value("${destination.email}") String destinationEmail, EmailTypeRepository emailTypeRepository) {
        this.clientUserRepository = clientUserRepository;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.destinationEmail = destinationEmail;
        this.emailTypeRepository = emailTypeRepository;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return clientUserRepository.findAll(pageable)
                .map(userMapper::userToUserDto);
    }

    @Override
    public UserDto add(UserCreateDto userCreateDto) {
        ClientUser clientUser = (ClientUser) userMapper.userCreateDtoToUser(userCreateDto);
        clientUserRepository.save(clientUser);
        System.out.println("User added: " + clientUser);
        System.out.println(clientUser.getVerified());

        Optional<EmailType> emailType = emailTypeRepository.findEmailTypeByType("ACTIVATION");
        if (!emailType.isPresent()) {
            throw new NotFoundException("Email type not found.");
        }
        EmailType emailType1 = emailType.get();

        EmailDto emailDto = userMapper.createEmailDtoToUser(userCreateDto, emailType1, clientUser.getVerified());
        emailDto.setRole(clientUser.getRole().getName());
        emailDto.setUserId(clientUser.getId().toString());
        emailDto.setType("ACTIVATION");

        jmsTemplate.convertAndSend(destinationEmail, messageHelper.createTextMessage(emailDto));

        return userMapper.userToUserDto(clientUser);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        ClientUser clientUser = clientUserRepository
                .findClientUserByEmailAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword()
                ).orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s not found.", tokenRequestDto.getUsername())));

        if (!clientUser.isActive()) {
            throw new IllegalStateException("User account is disabled.");
        }

        if (!clientUser.getVerified().equals("verified")) {
            throw new IllegalStateException("User account is not verified.");
        }

        Claims claims = Jwts.claims();
        claims.put("id", clientUser.getId());
        claims.put("role", clientUser.getRole().getName());
        String token = tokenService.generate(claims);

        return new TokenResponseDto(token);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        ClientUser clientUser = clientUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));

        clientUser.setEmail(userDto.getEmail());
        clientUser.setFirstName(userDto.getFirstName());
        clientUser.setLastName(userDto.getLastName());

        clientUserRepository.save(clientUser);
        return userMapper.userToUserDto(clientUser);
    }

    public UserDto findById(Long userId) {
        System.out.println("User id: " + userId);
        ClientUser clientUser = clientUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));

        return userMapper.userToUserDto(clientUser);
    }

    //Edit method
    public UserDto edit(Long userId, UserCreateDto userCreateDto) {
        ClientUser clientUser = clientUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));

        clientUser.setEmail(userCreateDto.getEmail());
        clientUser.setFirstName(userCreateDto.getFirstName());
        clientUser.setLastName(userCreateDto.getLastName());
        if (userCreateDto.getUserType() == null) {
            System.out.println(clientUser.getRole().getName());
            userCreateDto.setUserType(clientUser.getRole().getName());
        }
        if (userCreateDto.getPassword() != null && !userCreateDto.getPassword().equals(clientUser.getPassword())) {
            Optional<EmailType> emailType = emailTypeRepository.findEmailTypeByType("PASSWORD_CHANGED");
            if (!emailType.isPresent()) {
                throw new NotFoundException("Email type not found.");
            }
            EmailType emailType1 = emailType.get();
            EmailDto emailDto = userMapper.createEmailDtoToUser(userCreateDto, emailType1, clientUser.getVerified());
            emailDto.setRole(clientUser.getRole().getName());
            emailDto.setUserId(clientUser.getId().toString());
            emailDto.setType("PASSWORD_CHANGED");
            jmsTemplate.convertAndSend(destinationEmail, messageHelper.createTextMessage(emailDto));
        }


        clientUserRepository.save(clientUser);
        return userMapper.userToUserDto(clientUser);
    }

    public String updateVerficationToken(String token) {
        ClientUser clientUser = clientUserRepository.findClientUserByVerified(token)
                .orElseThrow(() -> new NotFoundException("User not found."));

        clientUser.setVerified("verified");

        clientUserRepository.save(clientUser);
        return "User verified";
    }

    public UserDto increaseTrainingCount(Long userId) {
        ClientUser clientUser = clientUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));

        clientUser.setScheduledTrainings(clientUser.getScheduledTrainings() + 1);

        clientUserRepository.save(clientUser);
        return userMapper.userToUserDto(clientUser);
    }

    public UserDto decreaseTrainingCount(Long userId) {
        ClientUser clientUser = clientUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));

        clientUser.setScheduledTrainings(clientUser.getScheduledTrainings() - 1);

        clientUserRepository.save(clientUser);
        return userMapper.userToUserDto(clientUser);
    }
}
