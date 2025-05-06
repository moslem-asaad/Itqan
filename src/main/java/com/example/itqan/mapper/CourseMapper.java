package com.example.itqan.mapper;

import com.example.itqan.dto.CourseRequestDTO;
import com.example.itqan.dto.CourseResponseDTO;
import com.example.itqan.model.Course;
import com.example.itqan.model.CourseTime;
import com.example.itqan.model.Student;
import com.example.itqan.model.Teacher;

import java.util.List;

public class CourseMapper {
    public static CourseResponseDTO toResponseDTO(Course course){
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.id = course.getId();
        dto.name = course.getName();
        dto.schedule = course.getSchedule();
        dto.teacherId = course.getTeacher().getId();
        dto.numOfStudents = course.getStudents() != null ? course.getStudents().size() : 0;
        return dto;
    }

    public static Course fromRequestDTO(Course course,CourseRequestDTO dto, Teacher teacher, List<Student> students){
//        Course course = new Course();
        if (dto.getName() !=null) course.setName(dto.getName());
        if (dto.getSchedule() != null) {
            List<CourseTime> times = dto.getSchedule().stream()
                    .map(t -> {
                        CourseTime ct = new CourseTime();
                        ct.setCourse(course);
                        ct.setDayOfWeek(t.getDayOfWeek());
                        ct.setStartTime(t.getStartTime());
                        ct.setEndTime(t.getEndTime());
                        return ct;
                    }).toList();
            course.setSchedule(times);
        }
        course.setTeacher(teacher);
        if (students!=null) course.setStudents(students);

        return course;
    }

}
