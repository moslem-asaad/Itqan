package com.example.itqan.mapper;

import com.example.itqan.dto.LessonDTO;
import com.example.itqan.dto.LessonResourceDTO;
import com.example.itqan.model.Lesson;

import java.util.List;

public class LessonMapper {
    public static LessonDTO toDTO(Lesson lesson) {
            List<LessonResourceDTO> resourceDTOs = lesson.getResources().stream().map(LessonResourceMapper::toDTO).toList();

        return new LessonDTO(lesson.getTitle(), lesson.getDescription(), lesson.getCourse().getId(), resourceDTOs);
    }
}

