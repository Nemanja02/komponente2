package com.komponente.servis1.repository;

import com.komponente.servis1.domain.ManagerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerUserRepository extends JpaRepository<ManagerUser, Long> {
    Optional<ManagerUser> findManagerUserByEmailAndPassword(String email, String password);
    // find by verified
    Optional<ManagerUser> findManagerUserByVerified(String verified);

}
