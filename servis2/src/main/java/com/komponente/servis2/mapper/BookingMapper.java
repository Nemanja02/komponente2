package com.komponente.servis2.mapper;

import com.komponente.servis2.dto.BookingDto;
import com.komponente.servis2.dto.validations.BookingCreateDto;
import com.komponente.servis2.entity.Booking;
import com.komponente.servis2.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapper {

    @Lazy
    @Autowired
    TrainingSessionMapper trainingSessionMapper;
    @Lazy
    @Autowired
    TrainingService trainingService;

    public BookingDto toDto(Booking entity) {
        com.komponente.servis2.dto.BookingDto dto = new com.komponente.servis2.dto.BookingDto();
        dto.setId(entity.getId());
        dto.setTrainingSession(entity.getTrainingSession());
        dto.setClientId(entity.getClientId());
        dto.setBookedAt(entity.getBookedAt());
        return dto;
    }

    public Iterable<BookingDto> toDto(Iterable<Booking> entities) {
        List<BookingDto> dtos = new ArrayList<>();
        for (Booking entity : entities) {
            dtos.add(toDto(entity));
        }
        return dtos;
    }

    public Booking toEntity(BookingDto dto) {
        com.komponente.servis2.entity.Booking entity = new com.komponente.servis2.entity.Booking();
        entity.setId(dto.getId());
        entity.setTrainingSession(dto.getTrainingSession());
        entity.setClientId(dto.getClientId());
        entity.setBookedAt(dto.getBookedAt());
        return entity;
    }

    public Booking toEntity(BookingCreateDto dto) {
        Booking entity = new Booking();
        entity.setClientId(dto.getClientId());
        entity.setTrainingSession(trainingSessionMapper.toEntity(trainingService.getSession(dto.getTrainingSessionId())));
        return entity;
    }
}
