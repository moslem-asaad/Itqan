package com.example.itqan.mapper;

import com.example.itqan.dto.CourseTimeDTO;
import com.example.itqan.model.Course;
import com.example.itqan.model.CourseTime;

import java.util.List;

public class CourseTimeMapper {

    public static CourseTimeDTO toDTO(CourseTime courseTime){
        CourseTimeDTO courseTimeDTO = new CourseTimeDTO(courseTime.getId(),
                courseTime.getCourse().getId()
                ,courseTime.getDayOfWeek()
                ,courseTime.getStartTime()
                ,courseTime.getEndTime(),
                courseTime.getNextLessonDate());

        return courseTimeDTO;
    }

    public static CourseTime fromDTO (CourseTimeDTO courseTimeDTO, Course course){
        CourseTime courseTime = new CourseTime();
        courseTime.setCourse(course);
        courseTime.setDayOfWeek(courseTimeDTO.getDayOfWeek());
        courseTime.setStartTime(courseTimeDTO.getStartTime());
        courseTime.setEndTime(courseTimeDTO.getEndTime());
        courseTime.setNextLessonDate(courseTimeDTO.getNextLessonDate());
        return courseTime;
    }

    public static List<CourseTimeDTO> generateCourseTimeDTOList(List<CourseTime> schedule) {
        return schedule.stream().map((CourseTimeMapper::toDTO)).toList();
    }
}
