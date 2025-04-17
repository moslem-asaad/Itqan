package com.example.itqan.service;

import com.example.itqan.dto.CourseRequestDTO;
import com.example.itqan.model.*;
import com.example.itqan.repository.CourseRepository;
import com.example.itqan.repository.StudentRepository;
import com.example.itqan.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository, TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByTeacher(int teacherId,Authentication authentication) throws IllegalAccessException {
        User user = (User) authentication.getPrincipal();

        if (user.getRole() == Role.TEACHER && user.getId() != teacherId) {
            throw new IllegalAccessException("Access denied.");
        }
        return courseRepository.findByTeacherId(teacherId);
    }

    public Optional<Course> getCourseById(int id) {
        return courseRepository.findById(id);
    }

    public Course saveCourse(CourseRequestDTO dto, Authentication authentication) throws IllegalAccessException {
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        User user = (User) authentication.getPrincipal();
        if (user.getId() != teacher.getId()){
            throw new IllegalAccessException("Access denied.");
        }

        List<Student> students = studentRepository.findAllById(dto.getStudentIds());

        Course course = new Course();
        course.setName(dto.getName());
        course.setSchedule(dto.getSchedule());
        course.setTeacher(teacher);
        course.setStudents(students);

        return courseRepository.save(course);
    }

    public void deleteCourse(int id,Authentication authentication) throws IllegalAccessException {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        User user = (User) authentication.getPrincipal();
        if (user.getId() != course.getTeacher().getId()){
            throw new IllegalAccessException("Access denied.");
        }
        courseRepository.deleteById(id);
    }


    @Transactional
    public Course updateCourse(int teacherId,int courseId, CourseRequestDTO dto,Authentication authentication) throws IllegalAccessException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        User user = (User) authentication.getPrincipal();

        if (user.getRole() == Role.TEACHER && user.getId() != teacherId) {
            throw new IllegalAccessException("Access denied.");
        }

        if (user.getRole() == Role.TEACHER && dto.getTeacherId() != user.getId()) {
            throw new IllegalAccessException("You can only edit your own courses.");
        }

        List<Student> students = studentRepository.findAllById(dto.getStudentIds());

        course.setName(dto.getName());
        course.setSchedule(dto.getSchedule());
        course.setTeacher(teacher);
        course.setStudents(students);

        return courseRepository.save(course);
    }

}
