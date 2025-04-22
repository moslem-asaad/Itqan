package com.example.itqan.service;

import com.example.itqan.dto.CourseRequestDTO;
import com.example.itqan.dto.CourseResponseDTO;
import com.example.itqan.exceptions.InvalidIdException;
import com.example.itqan.exceptions.ResourceNotFoundException;
import com.example.itqan.model.*;
import com.example.itqan.repository.CourseRepository;
import com.example.itqan.repository.StudentRepository;
import com.example.itqan.repository.TeacherRepository;
import jakarta.transaction.Transactional;
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

    public CourseService(CourseRepository courseRepository, TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
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
        return courses.stream().map(course -> {
            CourseResponseDTO dto = new CourseResponseDTO();
            dto.id = course.getId();
            dto.name = course.getName();
            dto.schedule = course.getSchedule();
            dto.teacherId = course.getTeacher().getId();
            dto.numOfStudents = course.getStudents() != null ? course.getStudents().size() : 0;
            return dto;
        }).collect(Collectors.toList());
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
                CourseResponseDTO dto = new CourseResponseDTO();
                dto.id = course.getId();
                dto.name = course.getName();
                dto.schedule = course.getSchedule();
                dto.teacherId = course.getTeacher().getId();
                dto.numOfStudents = course.getStudents() != null ? course.getStudents().size() : 0;
                return dto;
            }
        }
        throw new ResourceNotFoundException("Course Not Found");
    }

    public Optional<Course> getCourseById(int id) {
        return courseRepository.findById(id);
    }

    public Course saveCourse(CourseRequestDTO dto, Authentication authentication) throws IllegalAccessException {
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

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
        List<Integer> studentIds = dto.getStudentIds();
        if(studentIds!=null){
            List<Student> students = studentRepository.findAllById(studentIds);
            course.setStudents(students);
        }
        course.setName(dto.getName());
        course.setSchedule(dto.getSchedule());
        course.setTeacher(teacher);
        course = courseRepository.save(course);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO();
        courseResponseDTO.id = course.getId();
        courseResponseDTO.name = course.getName();
        courseResponseDTO.schedule = course.getSchedule();
        courseResponseDTO.teacherId = course.getTeacher().getId();
        courseResponseDTO.numOfStudents = course.getStudents() != null ? course.getStudents().size() : 0;
        return courseResponseDTO;
    }


}
