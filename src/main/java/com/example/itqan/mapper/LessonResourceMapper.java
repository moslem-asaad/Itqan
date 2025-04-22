package com.example.itqan.mapper;

import com.example.itqan.dto.LessonDTO;
import com.example.itqan.dto.LessonResourceDTO;
import com.example.itqan.model.Lesson;
import com.example.itqan.model.LessonResource;

import java.util.List;

public class LessonResourceMapper {
    public static LessonResourceDTO toDTO(LessonResource resource) {
        return new LessonResourceDTO(
                resource.getId(),resource.getName(),resource.getUrl(),resource.isInternal(),resource.getType());
    }
}
