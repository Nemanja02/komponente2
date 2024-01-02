package com.komponente.servis1.mapper;

import com.komponente.servis1.domain.EmailType;
import com.komponente.servis1.dto.EmailTypeDto;
import org.springframework.stereotype.Component;

@Component
public class EmailTypeMapper {
    public EmailTypeDto emailToEmailTypeDto(EmailType emailType) {
        EmailTypeDto emailTypeDto = new EmailTypeDto();
        emailTypeDto.setId(emailType.getId());
        emailTypeDto.setType(emailType.getType());
        emailTypeDto.setSubject(emailType.getSubject());
        emailTypeDto.setBody(emailType.getBody());
        return emailTypeDto;
    }

    public EmailType emailTypeDtoToEmail(EmailTypeDto emailTypeDto) {
        EmailType emailType = new EmailType();
        emailType.setId(emailTypeDto.getId());
        emailType.setType(emailTypeDto.getType());
        emailType.setSubject(emailTypeDto.getSubject());
        emailType.setBody(emailTypeDto.getBody());
        return emailType;
    }
}
