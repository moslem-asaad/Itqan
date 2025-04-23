package com.example.itqan.mapper;

import com.example.itqan.dto.LessonDTO;
import com.example.itqan.dto.LessonResourceDTO;
import com.example.itqan.model.Lesson;
import com.example.itqan.model.LessonResource;

import java.util.List;

public class LessonResourceMapper {
    public static LessonResourceDTO toDTO(LessonResource resource) {
        return new LessonResourceDTO(
                resource.getId(),resource.getName(),resource.getUrl(), resource.getUrlShort(), resource.isInternal(),resource.getType());
    }

    public static LessonResource fromDTO(LessonResourceDTO dto, Lesson lesson){
        LessonResource res = new LessonResource();
        res.setName(dto.name);
        res.setUrl(dto.url);
        res.setInternal(dto.internal);
        res.setType(dto.type);
        res.setLesson(lesson);
        if (dto.urlShort == null || dto.urlShort.isEmpty()){
            res.setUrlShort(dto.url);
        }else{
            res.setUrlShort(dto.urlShort);
        }
        return res;
    }
}
