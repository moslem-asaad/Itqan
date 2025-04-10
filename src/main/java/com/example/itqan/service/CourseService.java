package com.example.itqan.service;

import com.example.itqan.dto.CourseRequestDTO;
import com.example.itqan.model.Course;
import com.example.itqan.model.Student;
import com.example.itqan.model.Teacher;
import com.example.itqan.repository.CourseRepository;
import com.example.itqan.repository.StudentRepository;
import com.example.itqan.repository.TeacherRepository;
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

    public List<Course> getCoursesByTeacher(int teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    public Optional<Course> getCourseById(int id) {
        return courseRepository.findById(id);
    }

    public Course saveCourse(CourseRequestDTO dto) {
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        List<Student> students = studentRepository.findAllById(dto.getStudentIds());

        Course course = new Course();
        course.setName(dto.getName());
        course.setSchedule(dto.getSchedule());
        course.setTeacher(teacher);
        course.setStudents(students);

        return courseRepository.save(course);
    }

    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }
}
