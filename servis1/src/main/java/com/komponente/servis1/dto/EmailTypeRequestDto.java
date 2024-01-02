package com.komponente.servis1.dto;

import org.springframework.stereotype.Component;

@Component
public class EmailTypeRequestDto {
    private Long clientId;
    private Long managerId;

    public EmailTypeRequestDto() {
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public EmailTypeRequestDto(Long clientId, Long managerId) {
        this.clientId = clientId;
        this.managerId = managerId;
    }
}
