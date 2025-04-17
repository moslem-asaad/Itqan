package com.example.itqan.controller;

import com.example.itqan.dto.LessonDTO;
import com.example.itqan.dto.LessonRequestDTO;
import com.example.itqan.dto.LessonResourceDTO;
import com.example.itqan.model.Lesson;
import com.example.itqan.model.LessonResource;
import com.example.itqan.service.CourseService;
import com.example.itqan.service.LessonService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<LessonDTO> createLesson(@RequestBody LessonRequestDTO dto) {
        LessonDTO lessonDTO = lessonService.saveLesson(dto);
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
            @RequestBody LessonResourceDTO dto
    ) {
        LessonDTO lessonDTO = lessonService.addResourceToLesson(lessonId,dto);
        return ResponseEntity.ok(lessonDTO);
    }

    @DeleteMapping("/resources/{resourceId}")
    public ResponseEntity<?> deleteResource(@PathVariable int resourceId) {
        lessonService.deleteResource(resourceId);
        return ResponseEntity.ok("Resource deleted");
    }

    @PutMapping("/resources/{resourceId}")
    public ResponseEntity<?> updateResource(
            @PathVariable int resourceId,
            @RequestBody LessonResourceDTO dto
    ) {
        LessonResource resource = lessonService.updateResource(resourceId,dto);
        return ResponseEntity.ok(resource);
    }


}
