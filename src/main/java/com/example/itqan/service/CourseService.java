package com.example.itqan.service;

import com.example.itqan.dto.CourseRequestDTO;
import com.example.itqan.dto.CourseResponseDTO;
import com.example.itqan.exceptions.InvalidIdException;
import com.example.itqan.exceptions.ResourceNotFoundException;
import com.example.itqan.mapper.CourseMapper;
import com.example.itqan.model.*;
import com.example.itqan.repository.CourseRepository;
import com.example.itqan.repository.CourseTimeRepository;
import com.example.itqan.repository.StudentRepository;
import com.example.itqan.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    private final CourseTimeRepository courseTimeRepository;

    public CourseService(CourseRepository courseRepository,
                         TeacherRepository teacherRepository,
                         StudentRepository studentRepository,
                         CourseTimeRepository courseTimeRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.courseTimeRepository = courseTimeRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<CourseResponseDTO> getCoursesByTeacher(int teacherId, Authentication authentication) throws IllegalAccessException {
        User user = (User) authentication.getPrincipal();

        if (user.getRole() == Role.TEACHER && user.getId() != teacherId) {
            throw new IllegalAccessException("Access denied.");
        }
        List<Course> courses = courseRepository.findByTeacherIdWithStudentsAndTeacher(teacherId);
        return courses.stream().map(CourseMapper::toResponseDTO).collect(Collectors.toList());
    }

    public CourseResponseDTO getCourseByTeacher(int teacherId, int courseId, Authentication authentication) throws IllegalAccessException {
        User user = (User) authentication.getPrincipal();

        if (user.getRole() == Role.TEACHER && user.getId() != teacherId) {
            throw new IllegalAccessException("Access denied.");
        }
        Optional<Course> optional = courseRepository.findById(courseId);
        if (optional.isPresent()){
            Course course = optional.get();
            if (course.getOwnerTeacherId()!=teacherId){
                throw new IllegalAccessException("Access denied.");
            }
            else{
                return CourseMapper.toResponseDTO(course);
            }
        }
        throw new ResourceNotFoundException("Course Not Found");
    }

    public Optional<Course> getCourseById(int id) {
        return courseRepository.findById(id);
    }

    public CourseResponseDTO saveCourse(CourseRequestDTO dto, Authentication authentication) throws IllegalAccessException {
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        User user = (User) authentication.getPrincipal();
        if (user.getId() != teacher.getId()){
            throw new IllegalAccessException("Access denied.");
        }
        isValidSchedule(dto.getSchedule());
        List<Student> students = null;
        if (dto.getStudentIds()!=null)
            students = studentRepository.findAllById(dto.getStudentIds());

        List<CourseTime> existingTimes = courseTimeRepository.findByCourse_TeacherId(teacher.getId());

        for (CourseTime newTime : dto.getSchedule()) {
            validateNoOverlap(existingTimes, newTime);
        }

        Course course = new Course();
        CourseMapper.fromRequestDTO(course, dto, teacher, students);

        course = courseRepository.save(course);
        return CourseMapper.toResponseDTO(course);
    }

    private void isValidSchedule(List<CourseTime> schedule) {
        for (int i = 0; i < schedule.size(); i++) {
            CourseTime a = schedule.get(i);
            a.isVaild();
            for (int j = i + 1; j < schedule.size(); j++) {
                CourseTime b = schedule.get(j);
                if (a.getDayOfWeek() == b.getDayOfWeek()) {
                    boolean overlap = a.getStartTime().isBefore(b.getEndTime()) &&
                            a.getEndTime().isAfter(b.getStartTime());
                    if (overlap) {
                        throw new IllegalArgumentException(
                                "Schedule conflict between submitted times on " + a.getDayOfWeek() +
                                        ": " + a.getStartTime() + "–" + a.getEndTime() +
                                        " overlaps with " + b.getStartTime() + "–" + b.getEndTime()
                        );
                    }
                }
            }
        }
    }


    public void deleteCourse(int id,Authentication authentication) throws IllegalAccessException {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        User user = (User) authentication.getPrincipal();
        if (user.getId() != course.getTeacher().getId()){
            throw new IllegalAccessException("Access denied.");
        }
        courseRepository.deleteById(id);
    }


    @Transactional
    public CourseResponseDTO updateCourse(int teacherId,int courseId, CourseRequestDTO dto,Authentication authentication) throws IllegalAccessException {
        if (teacherId!=dto.getTeacherId()){
            throw new InvalidIdException("Invalid Params");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        User user = (User) authentication.getPrincipal();

        if (user.getRole() == Role.TEACHER && user.getId() != teacherId) {
            throw new IllegalAccessException("Access denied.");
        }

        if (user.getRole() == Role.TEACHER && dto.getTeacherId() != user.getId()) {
            throw new IllegalAccessException("You can only edit your own courses.");
        }

        List<Student> students = null;
        if (dto.getStudentIds()!=null)
            students = studentRepository.findAllById(dto.getStudentIds());

        List<CourseTime> existingTimes = courseTimeRepository.findByCourse_TeacherId(teacher.getId());

        if(dto.getSchedule()!=null) {
            isValidSchedule(dto.getSchedule());
            for (CourseTime newTime : dto.getSchedule()) {
                validateNoOverlap(existingTimes, newTime);
            }
        }

        CourseMapper.fromRequestDTO(course,dto,teacher,students);
        course = courseRepository.save(course);


        return CourseMapper.toResponseDTO(course);
    }

    private void validUser(int id, Authentication authentication) throws IllegalAccessException {
        User user = (User) authentication.getPrincipal();

        if (user.getRole() == Role.TEACHER && user.getId() != id) {
            throw new IllegalAccessException("Access denied.");
        }
    }

    public void validateNoOverlap(List<CourseTime> existingTimes, CourseTime newTime) {
        for (CourseTime ct : existingTimes) {
            if (ct.getDayOfWeek() == newTime.getDayOfWeek()) {
                boolean overlap = newTime.getStartTime().isBefore(ct.getEndTime())
                        && newTime.getEndTime().isAfter(ct.getStartTime());

                if (overlap) {
                    throw new IllegalArgumentException(
                            "Schedule conflict on " + newTime.getDayOfWeek() +
                                    " between " + ct.getStartTime() + "–" + ct.getEndTime()
                    );
                }
            }
        }
    }



}
