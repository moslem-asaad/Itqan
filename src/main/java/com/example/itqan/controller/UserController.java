package com.example.itqan.controller;


import com.example.itqan.dto.UsernameAvailabilityResponseDTO;
import com.example.itqan.service.CourseService;
import com.example.itqan.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://fox-one-promptly.ngrok-free.app"
})
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check-and-suggest")
    public ResponseEntity<UsernameAvailabilityResponseDTO> checkAndSuggest(@RequestParam String username) {
        return ResponseEntity.ok(userService.checkUsernameAndSuggest(username));
    }


}
