package com.example.itqan.mapper;

import com.example.itqan.dto.LessonDTO;
import com.example.itqan.dto.LessonRequestDTO;
import com.example.itqan.dto.LessonResourceDTO;
import com.example.itqan.model.Course;
import com.example.itqan.model.Lesson;
import com.example.itqan.model.LessonResource;

import java.util.List;

public class LessonMapper {
    public static LessonDTO toDTO(Lesson lesson) {
            List<LessonResourceDTO> resourceDTOs = lesson.getResources().stream().map(LessonResourceMapper::toDTO).toList();

        return new LessonDTO(lesson.getTitle(), lesson.getDescription(), lesson.getCourse().getId(), resourceDTOs);
    }

    public static  Lesson fromDTO(LessonRequestDTO lessonDTO, Course course){
        Lesson lesson = new Lesson();
        if (lessonDTO.title == null) lessonDTO.title = "Lesson Title";
        lesson.setTitle(lessonDTO.title);
        if (lessonDTO.description == null) lessonDTO.description = "Lesson Description";
        lesson.setDescription(lessonDTO.description);
        lesson.setCourse(course);
        List<LessonResource> resources = lessonDTO.resources.stream().map(resDto -> LessonResourceMapper.fromDTO(resDto,lesson)).toList();
        lesson.setResources(resources);
        return lesson;
    }
}

