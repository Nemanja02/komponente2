package com.komponente.servis3.mapper;

import com.komponente.servis3.domain.Notification;
import com.komponente.servis3.dto.NotificationDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationMapper {
    public NotificationDto toDto(Notification notification) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setSubject(notification.getSubject());
        notificationDto.setBody(notification.getBody());
        notificationDto.setRole(notification.getRole());
        notificationDto.setUserId(notification.getUserId());
        notificationDto.setBookedAt(notification.getBookedAt());
        notificationDto.setType(notification.getType());
        return notificationDto;
    }

    public Iterable<NotificationDto> toDto(Iterable<Notification> notifications) {
        List<NotificationDto> notificationDtos = new ArrayList<>();
        for (Notification notification : notifications) {
            notificationDtos.add(toDto(notification));
        }
        return notificationDtos;
    }
}
