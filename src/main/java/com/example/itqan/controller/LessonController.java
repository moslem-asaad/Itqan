package com.example.itqan.controller;

import com.example.itqan.dto.LessonDTO;
import com.example.itqan.dto.LessonRequestDTO;
import com.example.itqan.dto.LessonResourceDTO;
import com.example.itqan.model.Lesson;
import com.example.itqan.model.LessonResource;
import com.example.itqan.service.CourseService;
import com.example.itqan.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://fox-one-promptly.ngrok-free.app"
})
@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }
    @PostMapping
    public ResponseEntity<LessonDTO> createLesson(@RequestBody LessonRequestDTO dto, Authentication authentication) throws IllegalAccessException {
        LessonDTO lessonDTO = lessonService.saveLesson(dto,authentication);
        return ResponseEntity.ok(lessonDTO);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads/" + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return ResponseEntity.ok("/uploads/" + fileName);
    }

    @PostMapping("/{lessonId}/resources")
    public ResponseEntity<?> addResourceToLesson(
            @PathVariable int lessonId,
            @RequestBody LessonResourceDTO dto,
            Authentication authentication
    ) throws IllegalAccessException {
        LessonDTO lessonDTO = lessonService.addResourceToLesson(lessonId,dto,authentication);
        return ResponseEntity.ok(lessonDTO);
    }

    @DeleteMapping("/resources/{resourceId}")
    public ResponseEntity<?> deleteResource(@PathVariable int resourceId, Authentication authentication) throws IllegalAccessException {
        lessonService.deleteResource(resourceId,authentication);
        return ResponseEntity.ok("Resource deleted");
    }

    @PutMapping("/resources/{resourceId}")
    public ResponseEntity<?> updateResource(
            @PathVariable int resourceId,
            @RequestBody LessonResourceDTO dto, Authentication authentication
    ) throws IllegalAccessException {
        LessonResource resource = lessonService.updateResource(resourceId,dto,authentication);
        return ResponseEntity.ok(resource);
    }


}
