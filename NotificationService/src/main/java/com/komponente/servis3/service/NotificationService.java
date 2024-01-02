package com.komponente.servis3.service;

import com.komponente.servis3.domain.Notification;
import com.komponente.servis3.dto.EmailDto;
import com.komponente.servis3.mapper.NotificationMapper;
import com.komponente.servis3.dto.NotificationDto;
import com.komponente.servis3.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    NotificationMapper notificationMapper;

    public NotificationDto createNotification(EmailDto emailDto) {
        Notification notification = new Notification();
        notification.setSubject(emailDto.getSubject());
        notification.setBody(emailDto.getBody());
        notification.setRole(emailDto.getRole());
        notification.setUserId(emailDto.getUserId().toString());
        notification.setType(emailDto.getType());

        notificationRepository.save(notification);

        return notificationMapper.toDto(notification);
    }

    public Iterable<NotificationDto> getAllNotificationsForUser(Long userId, String role) {
        Iterable<Notification> notifications = notificationRepository.findAllByUserIdAndRole(userId.toString(), role);
        return notificationMapper.toDto(notifications);
    }

    public Iterable<NotificationDto> getAllNotifications() {
        Iterable<Notification> notifications = notificationRepository.findAll();
        return notificationMapper.toDto(notifications);
    }
}
