package com.example.itqan.model;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.itqan.repository.CourseTimeRepository;
import com.example.itqan.model.CourseTime;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CourseTimeScheduler {

    @Autowired
    private CourseTimeRepository courseTimeRepository;

    @Scheduled(cron = "0 0 1 * * ?") // runs every day at 1 AM
    public void updateNextLessonDates() {
        List<CourseTime> allTimes = courseTimeRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (CourseTime ct : allTimes) {
            if (ct.getNextLessonDate().isBefore(now)) {
                ct.setNextLessonDate(ct.getNextLessonDate().plusWeeks(1));
            }
        }
        courseTimeRepository.saveAll(allTimes);
    }
}
