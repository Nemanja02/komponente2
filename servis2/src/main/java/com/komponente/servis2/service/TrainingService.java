package com.komponente.servis2.service;

import com.komponente.servis2.client.emailservice.dto.EmailRequestDto;
import com.komponente.servis2.client.emailservice.dto.EmailTypeDto;
import com.komponente.servis2.client.userservice.dto.UserDto;
import com.komponente.servis2.dto.TrainingSessionDto;
import com.komponente.servis2.dto.TrainingTypeDto;
import com.komponente.servis2.dto.validations.TrainingSessionCreateDto;
import com.komponente.servis2.dto.validations.TrainingSessionResponseDto;
import com.komponente.servis2.dto.validations.TrainingTypeCreateDto;
import com.komponente.servis2.dto.validations.TrainingTypeResponseDto;
import com.komponente.servis2.entity.Booking;
import com.komponente.servis2.entity.TrainingSession;
import com.komponente.servis2.entity.TrainingType;
import com.komponente.servis2.listener.helper.MessageHelper;
import com.komponente.servis2.mapper.GymMapper;
import com.komponente.servis2.mapper.PricingMapper;
import com.komponente.servis2.mapper.TrainingSessionMapper;
import com.komponente.servis2.mapper.TrainingTypeMapper;
import com.komponente.servis2.repository.BookingRepository;
import com.komponente.servis2.repository.PricingRepository;
import com.komponente.servis2.repository.TrainingSessionRepository;
import com.komponente.servis2.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrainingService {
    @Autowired
    TrainingTypeRepository trainingTypeRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    TrainingSessionRepository trainingSessionRepository;
    @Autowired
    TrainingTypeMapper trainingTypeMapper;
    @Autowired
    TrainingSessionMapper trainingSessionMapper;
    @Lazy
    @Autowired
    GymService gymService;
    @Lazy
    @Autowired
    GymMapper gymMapper;
    @Autowired
    @Lazy
    PricingRepository pricingRepository;
    @Autowired
    RestTemplate emailServiceRestTemplate;
    @Autowired
    RestTemplate userServiceRestTemplate;
    private String destinationEmail;

    @Autowired
    private BookingService bookingService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private MessageHelper messageHelper;

    public TrainingService(@Value("${destination.email}") String destinationEmail) {
        this.destinationEmail = destinationEmail;
    }

    public TrainingTypeDto addType(TrainingTypeCreateDto trainingTypeCreateDto) {
        TrainingType trainingType = trainingTypeMapper.trainingTypeCreateDtoToTrainingType(trainingTypeCreateDto);
        trainingTypeRepository.save(trainingType);
        System.out.println("Training type added: " + trainingType);

        return trainingTypeMapper.trainingTypeToTrainingTypeDto(trainingType);
    }

    public TrainingTypeDto getType(Long id) {
        TrainingType trainingType = trainingTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("Training type not found"));
        System.out.println("Training type found: " + trainingType);

        return trainingTypeMapper.trainingTypeToTrainingTypeDto(trainingType);
    }

    public TrainingTypeResponseDto getTypeResponse(Long id) {
        TrainingType trainingType = trainingTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("Training type not found"));
        System.out.println("Training type found: " + trainingType);

        return trainingTypeMapper.trainingTypeToTrainingTypeResponseDto(trainingType);
    }

    public Iterable<TrainingTypeDto> getAllTypes() {
        Iterable<TrainingType> trainingTypes = trainingTypeRepository.findAll();
        System.out.println("Training types found: " + trainingTypes);

        return trainingTypeMapper.trainingTypeToTrainingTypeDto(trainingTypes);
    }

    public Iterable<TrainingTypeResponseDto> getAllTypesResponse() {
        Iterable<TrainingType> trainingTypes = trainingTypeRepository.findAll();
        System.out.println("Training types found: " + trainingTypes);

        return trainingTypeMapper.trainingTypeToTrainingTypeResponseDto(trainingTypes);
    }

    public TrainingTypeDto updateType(TrainingTypeCreateDto trainingTypeCreateDto, Long id) {
        TrainingType trainingType = trainingTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("Training type not found"));
        trainingType.setName(trainingTypeCreateDto.getName());
        trainingType.setGroup(trainingTypeCreateDto.isGroup());
        trainingTypeRepository.save(trainingType);
        System.out.println("Training type updated: " + trainingType);

        return trainingTypeMapper.trainingTypeToTrainingTypeDto(trainingType);
    }

    public TrainingTypeDto deleteType(Long id) {
        if (pricingRepository.existsByTrainingTypeId(id)) {
            pricingRepository.deleteByTrainingTypeId(id);
        }
        TrainingType trainingType = trainingTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("Training type not found"));
        trainingTypeRepository.delete(trainingType);
        System.out.println("Training type deleted: " + trainingType);

        return trainingTypeMapper.trainingTypeToTrainingTypeDto(trainingType);
    }

    public TrainingSessionDto addSession(TrainingSessionCreateDto trainingSessionDto) {
        Optional<Iterable<TrainingSession>> trainingSessions = trainingSessionRepository.findAllByGymId(trainingSessionDto.getGymId());
        if (trainingSessions.isPresent()) {
            for (TrainingSession trainingSession : trainingSessions.get()) {
                if (trainingSession.getDayOfWeek().equals(trainingSessionDto.getDayOfWeek())) {
                    int existingSessionStart = trainingSession.getStartHour() * 60 + trainingSession.getStartMinute();
                    int existingSessionEnd = trainingSession.getEndHour() * 60 + trainingSession.getEndMinute();
                    int newSessionStart = trainingSessionDto.getStartHour() * 60 + trainingSessionDto.getStartMinute();
                    int newSessionEnd = trainingSessionDto.getEndHour() * 60 + trainingSessionDto.getEndMinute();

                    // Check for overlap with existing sessions
                    if ((newSessionStart < existingSessionEnd && newSessionStart >= existingSessionStart) ||
                            (newSessionEnd > existingSessionStart && newSessionEnd <= existingSessionEnd) || (newSessionStart <= existingSessionStart && newSessionEnd >= existingSessionEnd)) {
                        throw new RuntimeException("Training session already exists in that time slot");
                    }
                }
            }
        }


        TrainingSession trainingSession = trainingSessionMapper.trainingSessionCreateDtoToTrainingSession(trainingSessionDto);
        trainingSessionRepository.save(trainingSession);
        bookingService.bookSession(trainingSession.getId(), trainingSession.getGym().getId());
        System.out.println("Training session added: " + trainingSession);

        return trainingSessionMapper.trainingSessionToTrainingSessionDto(trainingSession);
    }

    public TrainingSessionDto getSession(Long id) {
        TrainingSession trainingSession = trainingSessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Training session not found"));
        System.out.println("Training session found: " + trainingSession);

        return trainingSessionMapper.trainingSessionToTrainingSessionDto(trainingSession);
    }

    public Iterable<TrainingSessionDto> getAllSessions() {
        Iterable<TrainingSession> trainingSessions = trainingSessionRepository.findAll();
        System.out.println("Training sessions found: " + trainingSessions);

        return trainingSessionMapper.trainingSessionToTrainingSessionDto(trainingSessions);
    }

    public Iterable<TrainingSessionResponseDto> getAllSessionsByGym(Long gymId) {
        Optional<Iterable<TrainingSession>> trainingSessions = trainingSessionRepository.findAllByGymId(gymId);
        System.out.println("Training sessions found: " + trainingSessions);

        if (trainingSessions.isPresent()) {
            return trainingSessionMapper.toResponse(trainingSessions.get());
        } else {
            // empty list
            return new ArrayList<>();
        }
    }

    public TrainingSessionDto updateSession(TrainingSessionCreateDto trainingSessionDto, Long id) {
        TrainingSession trainingSession = trainingSessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Training session not found"));
        trainingSession.setDayOfWeek(trainingSessionDto.getDayOfWeek());
        trainingSession.setEndHour(trainingSessionDto.getEndHour());
        trainingSession.setEndMinute(trainingSessionDto.getEndMinute());
        trainingSession.setStartHour(trainingSessionDto.getStartHour());
        trainingSession.setStartMinute(trainingSessionDto.getStartMinute());
        trainingSession.setMaxParticipants(trainingSessionDto.getMaxParticipants());
        trainingSession.setTrainingType(trainingTypeRepository.findById(trainingSessionDto.getTrainingTypeId()).orElseThrow(() -> new RuntimeException("Training type not found")));
        trainingSession.setGym(gymMapper.gymDtoToGym(gymService.get(trainingSessionDto.getGymId())));
        trainingSessionRepository.save(trainingSession);
        System.out.println("Training session updated: " + trainingSession);

        return trainingSessionMapper.trainingSessionToTrainingSessionDto(trainingSession);
    }

    public TrainingSessionDto deleteSession(Long id) {
        TrainingSession trainingSession = trainingSessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Training session not found"));
        trainingSessionRepository.delete(trainingSession);
        System.out.println("Training session deleted: " + trainingSession);

        return trainingSessionMapper.trainingSessionToTrainingSessionDto(trainingSession);
    }

    private String replacePlaceholders(String body, TrainingSessionDto trainingSessionDto) {
        return body.replace("{trainingSessionId}", trainingSessionDto.getId().toString())
                .replace("{trainingType}", trainingSessionDto.getTrainingType().getName())
                .replace("{startTime}", trainingSessionDto.getStartHour() + ":" + trainingSessionDto.getStartMinute())
                .replace("{endTime}", trainingSessionDto.getEndHour() + ":" + trainingSessionDto.getEndMinute())
                .replace("{dayOfWeek}", numberToDayOfWeek(trainingSessionDto.getDayOfWeek()));
    }

    private String numberToDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            default:
                return "Sunday";
        }
    }

    public TrainingSessionDto cancelSession(Long id) {
        TrainingSession trainingSession = trainingSessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Training session not found"));
        trainingSession.setCanceled(true);
        trainingSessionRepository.save(trainingSession);

        Iterable<Booking> bookings = bookingRepository.findByTrainingSessionId(id);
        for (Booking booking : bookings) {
            ResponseEntity<EmailTypeDto> emailTypeEntity = null;
            try {
                emailTypeEntity = emailServiceRestTemplate.exchange(
                        "/type/TRAINING_SESSION_CANCELLED_BY_MANAGER",
                        HttpMethod.POST,
                        new HttpEntity<>(new EmailRequestDto(booking.getClientId(), 0L)),
                        EmailTypeDto.class
                );
                EmailTypeDto emailTypeDto = emailTypeEntity.getBody();
                emailTypeDto.setBody(replacePlaceholders(emailTypeDto.getBody(), trainingSessionMapper.trainingSessionToTrainingSessionDto(trainingSession) ));
                emailTypeDto.setRole("ROLE_USER");
                emailTypeDto.setUserId(booking.getClientId().toString());
                emailTypeDto.setType("TRAINING_SESSION_CANCELLED_BY_MANAGER");
                System.out.println(emailTypeDto.getBody());
                jmsTemplate.convertAndSend(destinationEmail, messageHelper.createTextMessage(emailTypeDto));
            } catch (Exception e) {
                System.out.println("Email service is not available");
            }

            try {
                userServiceRestTemplate.exchange(
                        "/client/decrease/" + booking.getClientId(),
                        HttpMethod.PUT,
                        null,
                        UserDto.class
                );
            } catch (Exception e) {
                System.out.println("User service is not available");
            }
        }

        // send email to manager
        ResponseEntity<EmailTypeDto> emailTypeEntity = null;
        try {
            emailTypeEntity = emailServiceRestTemplate.exchange(
                    "/type/TRAINING_SESSION_CANCELLED_BY_MANAGER",
                    HttpMethod.POST,
                    new HttpEntity<>(new EmailRequestDto(0L, trainingSession.getGym().getManagerId())),
                    EmailTypeDto.class
            );
            EmailTypeDto emailTypeDto = emailTypeEntity.getBody();
            emailTypeDto.setBody(replacePlaceholders(emailTypeDto.getBody(), trainingSessionMapper.trainingSessionToTrainingSessionDto(trainingSession) ));
            emailTypeDto.setRole("ROLE_USER");
            emailTypeDto.setUserId(trainingSession.getGym().getManagerId().toString());
            emailTypeDto.setType("TRAINING_SESSION_CANCELLED_BY_MANAGER");
            System.out.println(emailTypeDto.getBody());
            jmsTemplate.convertAndSend(destinationEmail, messageHelper.createTextMessage(emailTypeDto));
        } catch (Exception e) {
            System.out.println("Email service is not available");
        }


        System.out.println("Training session cancelled: " + trainingSession);

        return trainingSessionMapper.trainingSessionToTrainingSessionDto(trainingSession);
    }

    public void sendSessionReminder(Long sessionId) {
        TrainingSession trainingSession = trainingSessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Training session not found"));

        Iterable<Booking> bookings = bookingRepository.findByTrainingSessionId(sessionId);

        for (Booking booking : bookings) {
            ResponseEntity<EmailTypeDto> emailTypeEntity = null;
            try {
                emailTypeEntity = emailServiceRestTemplate.exchange(
                        "/type/TRAINING_SESSION_REMINDER",
                        HttpMethod.POST,
                        new HttpEntity<>(new EmailRequestDto(booking.getClientId(), 0L)),
                        EmailTypeDto.class
                );
                EmailTypeDto emailTypeDto = emailTypeEntity.getBody();
                emailTypeDto.setBody(replacePlaceholders(emailTypeDto.getBody(), trainingSessionMapper.trainingSessionToTrainingSessionDto(trainingSession) ));
                emailTypeDto.setRole("ROLE_USER");
                emailTypeDto.setUserId(booking.getClientId().toString());
                emailTypeDto.setType("TRAINING_SESSION_REMINDER");
                System.out.println(emailTypeDto.getBody());
                jmsTemplate.convertAndSend(destinationEmail, messageHelper.createTextMessage(emailTypeDto));
            } catch (Exception e) {
                System.out.println("Email service is not available");
            }
        }
    }


}
