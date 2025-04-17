package com.example.itqan.service;

import com.example.itqan.dto.LessonDTO;
import com.example.itqan.dto.LessonRequestDTO;
import com.example.itqan.dto.LessonResourceDTO;
import com.example.itqan.exceptions.ResourceNotFoundException;
import com.example.itqan.model.Course;
import com.example.itqan.model.Lesson;
import com.example.itqan.model.LessonResource;
import com.example.itqan.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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

    public LessonDTO saveLesson(LessonRequestDTO dto) {
        Course course = courseRepository.findById(dto.courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Lesson lesson = new Lesson();
        lesson.setTitle(dto.title);
        lesson.setDescription(dto.description);
        lesson.setCourse(course);

        List<LessonResource> resources = dto.resources.stream().map(resDto -> {
            LessonResource res = new LessonResource();
            res.setName(resDto.name);
            res.setUrl(resDto.url);
            res.setInternal(resDto.internal);
            res.setType(resDto.type);
            res.setLesson(lesson);
            return res;
        }).toList();

        lesson.setResources(resources);

        LessonDTO lessonDTO = new LessonDTO(lesson.getId(),lesson.getTitle(),lesson.getDescription(),lesson.getCourse().getId(),lesson.getResources());

        lessonRepository.save(lesson);
        return lessonDTO;
    }

    public LessonDTO addResourceToLesson(int lessonId, LessonResourceDTO dto){
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        LessonResource resource = new LessonResource();
        resource.setName(dto.name);
        resource.setUrl(dto.url);
        resource.setInternal(dto.internal);
        resource.setType(dto.type);
        resource.setLesson(lesson);

        lesson.getResources().add(resource);
        LessonDTO lessonDTO = new LessonDTO(lesson.getId(),lesson.getTitle(),lesson.getDescription(),lesson.getCourse().getId(),lesson.getResources());

        lessonRepository.save(lesson);

        return lessonDTO;
    }

    public boolean deleteResource(int resourceId){
        if (!lessonResourceRepository.existsById(resourceId)) {
            throw new ResourceNotFoundException("resource not found");
        }
        lessonResourceRepository.deleteById(resourceId);
        return true;
    }

    public LessonResource updateResource(int resourceId, LessonResourceDTO dto){
        LessonResource resource = lessonResourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        resource.setName(dto.name);
        resource.setUrl(dto.url);
        resource.setInternal(dto.internal);
        resource.setType(dto.type);

        return lessonResourceRepository.save(resource);
    }

}
