package com.example.itqan.service;

import com.example.itqan.dto.CourseTimeDTO;
import com.example.itqan.mapper.CourseTimeMapper;
import com.example.itqan.model.CourseTime;
import com.example.itqan.repository.CourseTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseTimeService {

    @Autowired
    private CourseTimeRepository courseTimeRepository;

    public List<CourseTimeDTO> getNextLessonsForTeacher(Long teacherId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime now = LocalDateTime.now();

        List<CourseTime> courseTimeList = courseTimeRepository.findNextLessonsByTeacher(teacherId, now, pageable);
        return CourseTimeMapper.generateCourseTimeDTOList(courseTimeList);
    }
}
