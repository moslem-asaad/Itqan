package com.example.itqan.dto;

import com.example.itqan.model.ResourceType;

public class LessonResourceDTO {
    public int id;
    public String name;
    public String url;
    public boolean internal;
    public ResourceType type;

    public LessonResourceDTO(){}

    public LessonResourceDTO(int id,String name, String url, boolean internal, ResourceType type) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.internal = internal;
        this.type = type;
    }
}

