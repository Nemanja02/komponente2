package com.komponente.servis2.entity;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "TrainingType")
public class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Gym gym;

    @ManyToOne(optional = false)
    private TrainingType trainingType;
    private Integer startHour;
    private Integer startMinute;

    private Integer endHour;
    private Integer endMinute;
    private Integer dayOfWeek;
    private Integer maxParticipants;
    private boolean isCanceled;
}
