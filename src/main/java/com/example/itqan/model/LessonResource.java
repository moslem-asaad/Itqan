package com.example.itqan.model;

import com.example.itqan.dto.LessonResourceDTO;
import com.example.itqan.exceptions.InvalidModelStateException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class LessonResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String url;

    private String urlShort;

    private boolean internal;

    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    @JsonBackReference
    private Lesson lesson;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public int getOwnerTeacherId() {
        if (lesson == null){
            throw new InvalidModelStateException("lesson is null");
        }
        return this.lesson.getOwnerTeacherId();

    }

    public String getUrlShort() {
        return urlShort;
    }

    public void setUrlShort(String urlShort) {
        this.urlShort = urlShort;
    }

    public void updateResource(LessonResourceDTO dto) {
        if (dto.name!=null && !name.equals(dto.name)){
            this.name = dto.name;
        }
        if (dto.url!=null && !url.equals(dto.url)){
            if (equalUrlAndShortURL()){
                this.urlShort = dto.url;
            }
            this.url = dto.url;
        }
        if (dto.urlShort!=null && !urlShort.equals(dto.urlShort)){
            this.urlShort = dto.urlShort;
        }
        if (dto.type!=null){
            this.type = dto.type;
        }
        //this.internal = dto.internal;
    }

    private boolean equalUrlAndShortURL(){
        return url.equals(urlShort);
    }
}
