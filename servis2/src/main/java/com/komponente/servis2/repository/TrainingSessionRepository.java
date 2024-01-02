package com.komponente.servis2.repository;

import com.komponente.servis2.entity.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {

    public Optional<Iterable<TrainingSession>> findAllByGymId(Long id);
}
