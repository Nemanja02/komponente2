package com.komponente.servis1.repository;

import com.komponente.servis1.domain.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {
    Optional<ClientUser> findClientUserByEmailAndPassword(String email, String password);
    Optional<ClientUser> findClientUserByVerified(String verified);

}
