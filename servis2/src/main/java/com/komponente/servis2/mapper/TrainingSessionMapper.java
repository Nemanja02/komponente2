package com.komponente.servis2.mapper;

import com.komponente.servis2.dto.BookingDto;
import com.komponente.servis2.dto.TrainingSessionDto;
import com.komponente.servis2.dto.validations.TrainingSessionCreateDto;
import com.komponente.servis2.dto.validations.TrainingSessionResponseDto;
import com.komponente.servis2.entity.Booking;
import com.komponente.servis2.entity.TrainingSession;
import com.komponente.servis2.service.BookingService;
import com.komponente.servis2.service.GymService;
import com.komponente.servis2.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingSessionMapper {
    @Lazy
    @Autowired
    private GymMapper gymMapper;
    @Lazy
    @Autowired
    private TrainingTypeMapper trainingTypeMapper;
    @Lazy
    @Autowired
    private BookingService bookingService;
    @Autowired
    private TrainingService trainingService;
    @Lazy
    @Autowired
    private GymService gymService;

    public TrainingSession trainingSessionCreateDtoToTrainingSession(TrainingSessionCreateDto trainingSessionCreateDto) {
        TrainingSession trainingSession = new TrainingSession();
        trainingSession.setStartHour(trainingSessionCreateDto.getStartHour());
        trainingSession.setStartMinute(trainingSessionCreateDto.getStartMinute());
        trainingSession.setEndHour(trainingSessionCreateDto.getEndHour());
        trainingSession.setEndMinute(trainingSessionCreateDto.getEndMinute());
        trainingSession.setDayOfWeek(trainingSessionCreateDto.getDayOfWeek());
        trainingSession.setMaxParticipants(trainingSessionCreateDto.getMaxParticipants());
        trainingSession.setGym(gymMapper.gymDtoToGym(gymService.get(trainingSessionCreateDto.getGymId())));
        trainingSession.setTrainingType(trainingTypeMapper.trainingTypeDtoToTrainingType(trainingService.getType(trainingSessionCreateDto.getTrainingTypeId())));
        trainingSession.setCanceled(false);
        return trainingSession;
    }

    public TrainingSessionDto trainingSessionToTrainingSessionDto(TrainingSession trainingSession) {
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto();
        trainingSessionDto.setId(trainingSession.getId());
        trainingSessionDto.setStartHour(trainingSession.getStartHour());
        trainingSessionDto.setStartMinute(trainingSession.getStartMinute());
        trainingSessionDto.setEndHour(trainingSession.getEndHour());
        trainingSessionDto.setEndMinute(trainingSession.getEndMinute());
        trainingSessionDto.setDayOfWeek(trainingSession.getDayOfWeek());
        trainingSessionDto.setMaxParticipants(trainingSession.getMaxParticipants());
        trainingSessionDto.setGym(gymMapper.gymToGymDto(trainingSession.getGym()));
        trainingSessionDto.setTrainingType(trainingTypeMapper.trainingTypeToTrainingTypeDto(trainingSession.getTrainingType()));
        trainingSessionDto.setCanceled(trainingSession.isCanceled());
        return trainingSessionDto;
    }

    public Iterable<TrainingSessionDto> trainingSessionToTrainingSessionDto(Iterable<TrainingSession> trainingSessions) {
        List<TrainingSessionDto> trainingSessionList = new ArrayList<>();
        for (TrainingSession trainingSession : trainingSessions) {
            trainingSessionList.add(trainingSessionToTrainingSessionDto(trainingSession));
        }

        return trainingSessionList;
    }

    public TrainingSession toEntity(TrainingSessionDto trainingSessionDto) {
        TrainingSession trainingSession = new TrainingSession();
        trainingSession.setId(trainingSessionDto.getId());
        trainingSession.setStartHour(trainingSessionDto.getStartHour());
        trainingSession.setStartMinute(trainingSessionDto.getStartMinute());
        trainingSession.setEndHour(trainingSessionDto.getEndHour());
        trainingSession.setEndMinute(trainingSessionDto.getEndMinute());
        trainingSession.setDayOfWeek(trainingSessionDto.getDayOfWeek());
        trainingSession.setMaxParticipants(trainingSessionDto.getMaxParticipants());
        trainingSession.setGym(gymMapper.gymDtoToGym(gymService.get(trainingSessionDto.getGym().getId())));
        trainingSession.setTrainingType(trainingTypeMapper.trainingTypeDtoToTrainingType(trainingService.getType(trainingSessionDto.getTrainingType().getId())));
        trainingSession.setCanceled(trainingSessionDto.isCanceled());

        return trainingSession;
    }

    public TrainingSessionResponseDto toResponse(TrainingSession trainingSession) {
        TrainingSessionResponseDto trainingSessionResponseDto = new TrainingSessionResponseDto();
        trainingSessionResponseDto.setId(trainingSession.getId());
        trainingSessionResponseDto.setDayOfWeek(trainingSession.getDayOfWeek());
        trainingSessionResponseDto.setEndHour(trainingSession.getEndHour());
        trainingSessionResponseDto.setEndMinute(trainingSession.getEndMinute());
        trainingSessionResponseDto.setStartHour(trainingSession.getStartHour());
        trainingSessionResponseDto.setStartMinute(trainingSession.getStartMinute());
        trainingSessionResponseDto.setMaxParticipants(trainingSession.getMaxParticipants());
        trainingSessionResponseDto.setTrainingType(trainingTypeMapper.trainingTypeToTrainingTypeResponseDto(trainingSession.getTrainingType()));
        trainingSessionResponseDto.setGym(gymMapper.gymToGymDto(trainingSession.getGym()));
        trainingSessionResponseDto.setCanceled(trainingSession.isCanceled());
        // set bookings
        List<BookingDto> bookingDtoList = new ArrayList<>();
        for (BookingDto booking : bookingService.getByTrainingSessionId(trainingSession.getId())) {
            bookingDtoList.add(booking);
        }
        trainingSessionResponseDto.setBookings(bookingDtoList);

        return trainingSessionResponseDto;
    }

    public Iterable<TrainingSessionResponseDto> toResponse(Iterable<TrainingSession> trainingSessions) {
        List<TrainingSessionResponseDto> trainingSessionResponseDtoList = new ArrayList<>();
        for (TrainingSession trainingSession : trainingSessions) {
            trainingSessionResponseDtoList.add(toResponse(trainingSession));
        }

        return trainingSessionResponseDtoList;
    }
}
