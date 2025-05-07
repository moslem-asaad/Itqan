package com.example.itqan.mapper;

import com.example.itqan.dto.CourseRequestDTO;
import com.example.itqan.dto.CourseResponseDTO;
import com.example.itqan.dto.CourseTimeDTO;
import com.example.itqan.model.Course;
import com.example.itqan.model.CourseTime;
import com.example.itqan.model.Student;
import com.example.itqan.model.Teacher;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CourseMapper {
    public static CourseResponseDTO toResponseDTO(Course course){
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.id = course.getId();
        dto.name = course.getName();
        dto.schedule  = CourseTimeMapper.generateCourseTimeDTOList(course.getSchedule());
        dto.teacherId = course.getTeacher().getId();
        dto.numOfStudents = course.getStudents() != null ? course.getStudents().size() : 0;
        return dto;
    }

    public static Course fromRequestDTO(Course course,CourseRequestDTO dto, Teacher teacher, List<Student> students){
        if (dto.getName() !=null) course.setName(dto.getName());
        if (dto.getSchedule() != null) {
            List<CourseTime> times = dto.getSchedule().stream()
                    .map(t -> {
                        CourseTime ct = new CourseTime();
                        ct.setCourse(course);
                        ct.setDayOfWeek(t.getDayOfWeek());
                        ct.setStartTime(t.getStartTime());
                        ct.setEndTime(t.getEndTime());
                        LocalDateTime nextLessonDate = calculateNextLessonDate(t.getDayOfWeek(), t.getStartTime());
                        ct.setNextLessonDate(nextLessonDate);
                        return ct;
                    }).toList();
            course.setSchedule(times);
        }
        course.setTeacher(teacher);
        if (students!=null) course.setStudents(students);

        return course;
    }

    public static LocalDateTime calculateNextLessonDate(DayOfWeek dayOfWeek, LocalTime startTime) {
        LocalDate today = LocalDate.now();
        DayOfWeek todayDow = today.getDayOfWeek();

        int daysUntilNext = (dayOfWeek.getValue() - todayDow.getValue() + 7) % 7;
        if (daysUntilNext == 0 && startTime.isBefore(LocalTime.now())) {
            daysUntilNext = 7;
        }

        LocalDate nextDate = today.plusDays(daysUntilNext);
        return LocalDateTime.of(nextDate, startTime);
    }


}
