package com.komponente.servis2.repository;

import com.komponente.servis2.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    int countByTrainingSessionId(Long trainingSessionId);
    void deleteByTrainingSessionIdAndClientId(Long trainingSessionId, Long clientId);
    Iterable<Booking> findByTrainingSessionId(Long trainingSessionId);
    Optional<Booking> findByTrainingSessionIdAndClientId(Long trainingSessionId, Long clientId);
}
