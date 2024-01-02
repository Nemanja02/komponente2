package com.komponente.servis2.scheduler;

import com.komponente.servis2.dto.TrainingSessionDto;
import com.komponente.servis2.dto.validations.TrainingSessionResponseDto;
import com.komponente.servis2.entity.TrainingSession;
import com.komponente.servis2.mapper.TrainingSessionMapper;
import com.komponente.servis2.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@Component
public class SessionReminderScheduler {
    @Autowired
    TaskScheduler taskScheduler;
    @Autowired
    @Lazy
    TrainingService trainingService;
    @Autowired
    @Lazy
    TrainingSessionMapper trainingSessionMapper;

    public void scheduleSessionReminder(TrainingSessionDto trainingSession) {
        Runnable task = () -> {
//            TrainingSessionResponseDto session = trainingSessionMapper.toResponse(trainingSessionMapper.toEntity(trainingService.getSession(trainingSession.getId())));
//            if (session.isCanceled()) return;
//            if (session.getTrainingType().isGroup()) {
//                int bookingsSize = ((Collection<?>) session.getBookings()).size();
//
//                if (bookingsSize < 3) {
//                    trainingService.cancelSession(trainingSession.getId());
//                    return;
//                }
//            }
//
//            trainingService.sendSessionReminder(trainingSession.getId());
        };

        Date dateOfSession = getDateOfSession(trainingSession);
        Date dateOfReminder = new Date(dateOfSession.getTime() - 24 * 60 * 60 * 1000);
        taskScheduler.schedule(task, dateOfReminder);
    }

    private Date getDateOfSession(TrainingSessionDto trainingSession) {
        Date now = new Date();
        int dayOfWeek = now.getDay();
        int dayOfWeekOfSession = trainingSession.getDayOfWeek();
        int difference = dayOfWeekOfSession - dayOfWeek;
        Date dateOfSession = new Date(now.getTime() + difference * 24 * 60 * 60 * 1000);
        dateOfSession.setHours(trainingSession.getStartHour());
        dateOfSession.setMinutes(trainingSession.getStartMinute());

        return dateOfSession;
    }
}
