package com.komponente.servis2.service;

import com.komponente.servis2.client.emailservice.dto.EmailRequestDto;
import com.komponente.servis2.client.emailservice.dto.EmailTypeDto;
import com.komponente.servis2.client.userservice.dto.UserDto;
import com.komponente.servis2.dto.BookingDto;
import com.komponente.servis2.dto.validations.TrainingSessionResponseDto;
import com.komponente.servis2.entity.Gym;
import com.komponente.servis2.entity.TrainingSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class UserService {

    @Autowired
    RestTemplate userServiceRestTemplate;
    @Autowired
    @Lazy
    TrainingService trainingService;

    public UserDto getClient(Long id) {
        ResponseEntity<UserDto> userEntity = null;
        try {
            userEntity = userServiceRestTemplate.exchange(
                    "/client/" + id,
                    HttpMethod.GET,
                    null,
                    UserDto.class
            );
            UserDto user = userEntity.getBody();
            return user;
        } catch (Exception e) {
            System.out.println("User service is not available");
            throw new RuntimeException("User service is not available");
        }
    }

    public UserDto getManager(Long id) {
        ResponseEntity<UserDto> userEntity = null;
        try {
            userEntity = userServiceRestTemplate.exchange(
                    "/manager/" + id,
                    HttpMethod.GET,
                    null,
                    UserDto.class
            );
            UserDto user = userEntity.getBody();
            return user;
        } catch (Exception e) {
            System.out.println("User service is not available");
            throw new RuntimeException("User service is not available");
        }
    }

    public Integer getTrainingCountForGym(Gym gym, Long userId) {
        Iterable<TrainingSessionResponseDto> trainingSessions = trainingService.getAllSessionsByGym(gym.getId());
        Integer count = 0;
        for (TrainingSessionResponseDto trainingSession : trainingSessions) {
            if (trainingSession.isCanceled()) continue;
            for (BookingDto booking : trainingSession.getBookings()) {
                if (booking.getClientId().equals(userId)) {
                    count++;
                }
            }
        }

        return count;
    }
}
