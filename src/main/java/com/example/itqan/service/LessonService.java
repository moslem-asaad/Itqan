package com.example.itqan.service;
import com.example.itqan.dto.LessonDTO;
import com.example.itqan.dto.LessonRequestDTO;
import com.example.itqan.dto.LessonResourceDTO;
import com.example.itqan.exceptions.ResourceNotFoundException;
import com.example.itqan.mapper.LessonMapper;
import com.example.itqan.mapper.LessonResourceMapper;
import com.example.itqan.model.*;
import com.example.itqan.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class LessonService {
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final LessonResourceRepository lessonResourceRepository;


    public LessonService(CourseRepository courseRepository, LessonRepository lessonRepository, LessonResourceRepository lessonResourceRepository) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.lessonResourceRepository = lessonResourceRepository;
    }

    public LessonDTO saveLesson(LessonRequestDTO dto, Authentication authentication) throws IllegalAccessException {
        Course course = courseRepository.findById(dto.courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        User user = (User) authentication.getPrincipal();
        if (user.getId() != course.getOwnerTeacherId()){
            throw new IllegalAccessException("Access Denied, The course belongs to someone else");
        }
        Lesson lesson = LessonMapper.fromDTO(dto,course);
        lesson.validate();
        lessonRepository.save(lesson);
        return LessonMapper.toDTO(lesson);
    }

    /**
     * could change to jest return the resource
     * @param lessonId
     * @param dto
     * @param authentication
     * @return
     * @throws IllegalAccessException
     */
    public LessonDTO addResourceToLesson(int lessonId, LessonResourceDTO dto, Authentication authentication) throws IllegalAccessException {
        Lesson lesson = validParams(lessonId, authentication);
        LessonResource resource = LessonResourceMapper.fromDTO(dto,lesson);
        lesson.addResource(resource);
        lessonRepository.save(lesson);
        return LessonMapper.toDTO(lesson);
    }

    public void deleteLesson(int lessonId, Authentication authentication) throws IllegalAccessException {
        validParams(lessonId, authentication);
        lessonRepository.deleteById(lessonId);
    }

    public boolean deleteResource(int resourceId, Authentication authentication) throws IllegalAccessException {
        LessonResource lessonResource = lessonResourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        User user = (User) authentication.getPrincipal();
        if (user.getId() != lessonResource.getOwnerTeacherId()){
            throw new IllegalAccessException("Access Denied, The course belongs to someone else");
        }

        lessonResourceRepository.deleteById(resourceId);
        return true;
    }

    public LessonResourceDTO updateResource(int resourceId, LessonResourceDTO dto, Authentication authentication) throws IllegalAccessException {
        LessonResource resource = lessonResourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        User user = (User) authentication.getPrincipal();
        if (user.getId() != resource.getOwnerTeacherId()){
            throw new IllegalAccessException("Access Denied, The course belongs to someone else");
        }
        resource.updateResource(dto);
        return LessonResourceMapper.toDTO(lessonResourceRepository.save(resource));
    }

    public List<LessonDTO> getCourseLessons(int teacherId, int courseId, Authentication authentication) throws IllegalAccessException {
        User user = (User) authentication.getPrincipal();

        if (user.getRole() == Role.TEACHER && user.getId() != teacherId) {
            throw new IllegalAccessException("Access denied.");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (course.getTeacher().getId() != teacherId) {
            throw new IllegalAccessException("You are not authorized to access this course.");
        }

        List<LessonDTO> lessonDTOs = course.getLessons().stream()
                .map(LessonMapper::toDTO)
                .toList();

        return lessonDTOs;
    }

    private Lesson validParams(int lessonId, Authentication authentication) throws IllegalAccessException {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        User user = (User) authentication.getPrincipal();
        if (user.getId() != lesson.getOwnerTeacherId()){
            throw new IllegalAccessException("Access Denied, The course belongs to someone else");
        }
        return lesson;
    }


}
