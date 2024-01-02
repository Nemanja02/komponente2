package com.komponente.servis1.dto;

public class EmailTypeDto {
    private String type;
    private String subject;
    private String body;
    private Long id;

    public EmailTypeDto() {
    }

    public EmailTypeDto(String type, String subject, String body) {
        this.type = type;
        this.subject = subject;
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id= id;
    }
}
