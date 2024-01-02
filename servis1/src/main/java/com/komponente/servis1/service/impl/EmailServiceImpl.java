package com.komponente.servis1.service.impl;

import com.komponente.servis1.dto.EmailDto;
import com.komponente.servis1.domain.EmailType;
import com.komponente.servis1.dto.EmailTypeDto;
import com.komponente.servis1.dto.UserDto;
import com.komponente.servis1.mapper.EmailTypeMapper;
import com.komponente.servis1.mapper.UserMapper;
import com.komponente.servis1.repository.EmailTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmailServiceImpl {
    EmailTypeRepository emailTypeRepository;
    EmailTypeMapper emailTypeMapper;
    @Autowired
    ClientUserServiceImpl clientUserService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ManagerUserServiceImpl managerUserService;

    public EmailServiceImpl(EmailTypeRepository emailTypeRepository, EmailTypeMapper emailTypeMapper) {
        this.emailTypeRepository = emailTypeRepository;
        this.emailTypeMapper = emailTypeMapper;
    }

    public Page<EmailTypeDto> findAll(Pageable pageable) {
        return emailTypeRepository.findAll(pageable).map(emailTypeMapper::emailToEmailTypeDto);
    }

    public EmailTypeDto update(Long id, EmailTypeDto emailTypeDto) {
        emailTypeRepository.findById(id).map(emailType -> {
            emailType.setType(emailTypeDto.getType());
            emailType.setSubject(emailTypeDto.getSubject());
            emailType.setBody(emailTypeDto.getBody());
            return emailTypeRepository.save(emailType);
        }).orElseThrow(() -> new RuntimeException("Email not found"));
        return emailTypeDto;
    }

    public EmailTypeDto findById(Long id) {
        return emailTypeRepository.findById(id).map(emailTypeMapper::emailToEmailTypeDto).orElseThrow(() -> new RuntimeException("Email not found"));
    }

    public EmailDto findByType(String type, Long clientId, Long managerId) {
        EmailType emailType = emailTypeRepository.findEmailTypeByType(type).orElseThrow(() -> new RuntimeException("Email not found"));
        UserDto userDto = null;
        if (clientId != 0) {
            userDto = clientUserService.findById(clientId);
        } else if (managerId != null) {
            userDto = managerUserService.findById(managerId);
        }
        System.out.println("------------------");
        System.out.println(userDto.getEmail());
        EmailDto emailDto = userMapper.userDtoToEmailDto(userDto, emailType, "");

        return emailDto;
    }

}
