package com.komponente.servis3.repository;

import com.komponente.servis3.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Iterable<Notification> findAllByUserIdAndRole(String userId, String role);
}
