package com.komponente.servis3.controller;

import com.komponente.servis3.dto.NotificationRequestDto;
import com.komponente.servis3.dto.NotificationDto;
import com.komponente.servis3.service.NotificationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/notification")
public class Controller {
    @Autowired
    NotificationService notificationService;

    @ApiOperation(value = "Get all notifications for user")
    @PostMapping("/user/{userId}")
    public ResponseEntity<Iterable<NotificationDto>> getAllNotificationsForUser(@PathVariable Long userId, @Valid @RequestBody NotificationRequestDto notificationRequestDto) {
        return ResponseEntity.ok(notificationService.getAllNotificationsForUser(userId, notificationRequestDto.getRole()));
    }

    @ApiOperation(value = "Get all notifications")
    @GetMapping("/all")
    public ResponseEntity<Iterable<NotificationDto>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }
}
