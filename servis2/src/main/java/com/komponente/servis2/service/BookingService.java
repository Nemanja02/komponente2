package com.komponente.servis2.service;

import com.komponente.servis2.client.emailservice.EmailServiceClientConfig;
import com.komponente.servis2.client.emailservice.dto.EmailRequestDto;
import com.komponente.servis2.client.emailservice.dto.EmailTypeDto;
import com.komponente.servis2.client.userservice.dto.UserDto;
import com.komponente.servis2.dto.BookingDto;
import com.komponente.servis2.dto.TrainingSessionDto;
import com.komponente.servis2.dto.validations.BookingCreateDto;
import com.komponente.servis2.entity.Booking;
import com.komponente.servis2.entity.Gym;
import com.komponente.servis2.entity.TrainingSession;
import com.komponente.servis2.entity.TrainingType;
import com.komponente.servis2.listener.helper.MessageHelper;
import com.komponente.servis2.mapper.BookingMapper;
import com.komponente.servis2.mapper.GymMapper;
import com.komponente.servis2.mapper.TrainingSessionMapper;
import com.komponente.servis2.mapper.TrainingTypeMapper;
import com.komponente.servis2.repository.BookingRepository;
import com.komponente.servis2.scheduler.SessionReminderScheduler;
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

@Service
@Transactional
public class BookingService {
    @Autowired
    TrainingService trainingService;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    BookingMapper bookingMapper;
    @Autowired
    RestTemplate emailServiceRestTemplate;
    @Autowired
    RestTemplate userServiceRestTemplate;
    private String destinationEmail;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private MessageHelper messageHelper;

    @Autowired
    private UserService userService;
    @Autowired
    private SessionReminderScheduler sessionReminderScheduler;
    @Autowired
    @Lazy
    private LoyaltyService loyaltyService;
    @Autowired
    @Lazy
    private GymMapper gymMapper;
    @Autowired
    @Lazy
    private TrainingTypeMapper trainingTypeMapper;

    public BookingService(@Value("${destination.email}") String destinationEmail) {
        this.destinationEmail = destinationEmail;
    }


    public TrainingSessionDto bookSession(BookingCreateDto bookingCreateDto) {
        TrainingSessionDto trainingSessionDto = trainingService.getSession(bookingCreateDto.getTrainingSessionId());
        if (bookingRepository.findByTrainingSessionIdAndClientId(bookingCreateDto.getTrainingSessionId(), bookingCreateDto.getClientId()).isPresent()) {
            throw new RuntimeException("Already booked");
        }
        if (trainingSessionDto.isCanceled()) {
            throw new RuntimeException("Session is canceled");
        }
        if (trainingSessionDto.getMaxParticipants() > bookingRepository.countByTrainingSessionId(bookingCreateDto.getTrainingSessionId())) {
            bookingRepository.save(bookingMapper.toEntity(bookingCreateDto));

            ResponseEntity<EmailTypeDto> emailTypeEntity = null;
            try {
                emailTypeEntity = emailServiceRestTemplate.exchange(
                        "/type/TRAINING_SESSION_BOOKED",
                        HttpMethod.POST,
                        new HttpEntity<>(new EmailRequestDto(bookingCreateDto.getClientId(), 0L)),
                        EmailTypeDto.class
                );
                EmailTypeDto emailTypeDto = emailTypeEntity.getBody();
                emailTypeDto.setBody(replacePlaceholders(emailTypeDto.getBody(), trainingService.getSession(bookingCreateDto.getTrainingSessionId())));
                // get gym
                Gym gym = gymMapper.gymDtoToGym(trainingService.getSession(bookingCreateDto.getTrainingSessionId()).getGym());
                TrainingType trainingType = trainingTypeMapper.trainingTypeDtoToTrainingType(trainingService.getSession(bookingCreateDto.getTrainingSessionId()).getTrainingType());
                Float price = loyaltyService.getPrice(gym, trainingType, bookingCreateDto.getClientId());
                emailTypeDto.setBody(emailTypeDto.getBody().replace("{price}", price.toString()));
                emailTypeDto.setRole("ROLE_USER");
                emailTypeDto.setUserId(bookingCreateDto.getClientId().toString());
                emailTypeDto.setType("TRAINING_SESSION_BOOKED");
                System.out.println(emailTypeDto.getBody());
                jmsTemplate.convertAndSend(destinationEmail, messageHelper.createTextMessage(emailTypeDto));
            } catch (Exception e) {
                System.out.println("Email service is not available");
            }
            emailTypeEntity = null;
            try {
                emailTypeEntity = emailServiceRestTemplate.exchange(
                        "/type/TRAINING_SESSION_BOOKED_BY_CLIENT",
                        HttpMethod.POST,
                        new HttpEntity<>(new EmailRequestDto(0L, trainingService.getSession(bookingCreateDto.getTrainingSessionId()).getGym().getManagerId())),
                        EmailTypeDto.class
                );
                EmailTypeDto emailTypeDto = emailTypeEntity.getBody();
                emailTypeDto.setBody(replacePlaceholders(emailTypeDto.getBody(), trainingService.getSession(bookingCreateDto.getTrainingSessionId())));
                emailTypeDto.setRole("ROLE_MANAGER");
                emailTypeDto.setUserId(trainingService.getSession(bookingCreateDto.getTrainingSessionId()).getGym().getManagerId().toString());
                emailTypeDto.setType("TRAINING_SESSION_BOOKED_BY_CLIENT");
                System.out.println(emailTypeDto.getBody());
                jmsTemplate.convertAndSend(destinationEmail, messageHelper.createTextMessage(emailTypeDto));
            } catch (Exception e) {
                System.out.println("Email service is not available");
            }
            try {
                userServiceRestTemplate.exchange(
                        "/client/increase/" + bookingCreateDto.getClientId(),
                        HttpMethod.PUT,
                        null,
                        UserDto.class
                );
            } catch (Exception e) {
                System.out.println("User service is not available");
            }

            sessionReminderScheduler.scheduleSessionReminder(trainingService.getSession(bookingCreateDto.getTrainingSessionId()));

            return trainingService.getSession(bookingCreateDto.getTrainingSessionId());
        } else {
            throw new RuntimeException("Session is full");
        }
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

    public TrainingSessionDto bookSession(Long trainingSessionId, Long clientId) {
        if (bookingRepository.findByTrainingSessionIdAndClientId(trainingSessionId, clientId).isPresent()) {
            throw new RuntimeException("Already booked");
        }
        if (trainingService.getSession(trainingSessionId).isCanceled()) {
            throw new RuntimeException("Session is canceled");
        }
        if (trainingService.getSession(trainingSessionId).getMaxParticipants() > bookingRepository.countByTrainingSessionId(trainingSessionId)) {
            bookingRepository.save(bookingMapper.toEntity(new BookingCreateDto(trainingSessionId, clientId)));

            ResponseEntity<EmailTypeDto> emailTypeEntity = null;
            try {
                emailTypeEntity = emailServiceRestTemplate.exchange(
                        "/type/TRAINING_SESSION_BOOKED",
                        HttpMethod.POST,
                        new HttpEntity<>(new EmailRequestDto(clientId, 0L)),
                        EmailTypeDto.class
                );
                EmailTypeDto emailTypeDto = emailTypeEntity.getBody();
                emailTypeDto.setBody(replacePlaceholders(emailTypeDto.getBody(), trainingService.getSession(trainingSessionId)));
                // get gym
                Gym gym = gymMapper.gymDtoToGym(trainingService.getSession(trainingSessionId).getGym());
                TrainingType trainingType = trainingTypeMapper.trainingTypeDtoToTrainingType(trainingService.getSession(trainingSessionId).getTrainingType());
                Float price = loyaltyService.getPrice(gym, trainingType, clientId);
                emailTypeDto.setBody(emailTypeDto.getBody().replace("{price}", price.toString()));
                emailTypeDto.setRole("ROLE_USER");
                emailTypeDto.setUserId(clientId.toString());
                emailTypeDto.setType("TRAINING_SESSION_BOOKED");
                System.out.println(emailTypeDto.getBody());
                jmsTemplate.convertAndSend(destinationEmail, messageHelper.createTextMessage(emailTypeDto));
            } catch (Exception e) {
                System.out.println("Email service is not available");
            }
            emailTypeEntity = null;
            try {
                emailTypeEntity = emailServiceRestTemplate.exchange(
                        "/type/TRAINING_SESSION_BOOKED_BY_CLIENT",
                        HttpMethod.POST,
                        new HttpEntity<>(new EmailRequestDto(0L, trainingService.getSession(trainingSessionId).getGym().getManagerId())),
                        EmailTypeDto.class
                );
                EmailTypeDto emailTypeDto = emailTypeEntity.getBody();
                emailTypeDto.setBody(replacePlaceholders(emailTypeDto.getBody(), trainingService.getSession(trainingSessionId)));
                emailTypeDto.setRole("ROLE_MANAGER");
                emailTypeDto.setUserId(trainingService.getSession(trainingSessionId).getGym().getManagerId().toString());
                emailTypeDto.setType("TRAINING_SESSION_BOOKED_BY_CLIENT");
                System.out.println(emailTypeDto.getBody());
                jmsTemplate.convertAndSend(destinationEmail, messageHelper.createTextMessage(emailTypeDto));
            } catch (Exception e) {
                System.out.println("Email service is not available");
            }
            try {
                userServiceRestTemplate.exchange(
                        "/client/increase/" + clientId,
                        HttpMethod.PUT,
                        null,
                        UserDto.class
                );
            } catch (Exception e) {
                System.out.println("User service is not available");
            }

            sessionReminderScheduler.scheduleSessionReminder(trainingService.getSession(trainingSessionId));

            return trainingService.getSession(trainingSessionId);
        } else {
            throw new RuntimeException("Session is full");
        }
    }

    public void cancelBooking(BookingCreateDto bookingCreateDto) {
        if (trainingService.getSession(bookingCreateDto.getTrainingSessionId()).isCanceled()) {
            throw new RuntimeException("Session is canceled");
        }
        bookingRepository.deleteByTrainingSessionIdAndClientId(bookingCreateDto.getTrainingSessionId(), bookingCreateDto.getClientId());
        if (bookingRepository.countByTrainingSessionId(bookingCreateDto.getTrainingSessionId()) == 0) {
            ResponseEntity<EmailTypeDto> emailTypeEntity = null;
            try {
                emailTypeEntity = emailServiceRestTemplate.exchange(
                        "/type/TRAINING_SESSION_CANCELLED",
                        HttpMethod.POST,
                        new HttpEntity<>(new EmailRequestDto(bookingCreateDto.getClientId(), 0L)),
                        EmailTypeDto.class
                );
                EmailTypeDto emailTypeDto = emailTypeEntity.getBody();
                emailTypeDto.setBody(replacePlaceholders(emailTypeDto.getBody(), trainingService.getSession(bookingCreateDto.getTrainingSessionId())));
                emailTypeDto.setRole("ROLE_USER");
                emailTypeDto.setUserId(bookingCreateDto.getClientId().toString());
                emailTypeDto.setType("TRAINING_SESSION_CANCELLED");
                System.out.println(emailTypeDto.getBody());
                jmsTemplate.convertAndSend(destinationEmail, messageHelper.createTextMessage(emailTypeDto));
            } catch (Exception e) {
                System.out.println("Email service is not available");
            }

            emailTypeEntity = null;
            try {
                emailTypeEntity = emailServiceRestTemplate.exchange(
                        "/type/TRAINING_SESSION_CANCELLED_BY_CLIENT",
                        HttpMethod.POST,
                        new HttpEntity<>(new EmailRequestDto(0L, trainingService.getSession(bookingCreateDto.getTrainingSessionId()).getGym().getManagerId())),
                        EmailTypeDto.class
                );
                EmailTypeDto emailTypeDto = emailTypeEntity.getBody();
                emailTypeDto.setBody(replacePlaceholders(emailTypeDto.getBody(), trainingService.getSession(bookingCreateDto.getTrainingSessionId())));
                emailTypeDto.setRole("ROLE_MANAGER");
                emailTypeDto.setUserId(trainingService.getSession(bookingCreateDto.getTrainingSessionId()).getGym().getManagerId().toString());
                emailTypeDto.setType("TRAINING_SESSION_CANCELLED_BY_CLIENT");
                System.out.println(emailTypeDto.getBody());
                jmsTemplate.convertAndSend(destinationEmail, messageHelper.createTextMessage(emailTypeDto));
            } catch (Exception e) {
                System.out.println("Email service is not available");
            }

            try {
                userServiceRestTemplate.exchange(
                        "/client/decrease/" + bookingCreateDto.getClientId(),
                        HttpMethod.PUT,
                        null,
                        UserDto.class
                );
            } catch (Exception e) {
                System.out.println("User service is not available");
            }

            trainingService.deleteSession(bookingCreateDto.getTrainingSessionId());
        }
    }

    public Iterable<BookingDto> getByTrainingSessionId(Long trainingSessionId) {
        return bookingMapper.toDto(bookingRepository.findByTrainingSessionId(trainingSessionId));
    }
}
