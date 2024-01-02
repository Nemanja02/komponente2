package com.komponente.servis1.repository;

import com.komponente.servis1.domain.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findAdminUserByEmailAndPassword(String email, String password);
}
