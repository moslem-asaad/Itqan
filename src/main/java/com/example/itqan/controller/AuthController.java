package com.example.itqan.controller;

import com.example.itqan.dto.AuthRequest;
import com.example.itqan.dto.RegisterRequest;
import com.example.itqan.dto.UserDTO;
import com.example.itqan.model.*;
import com.example.itqan.repository.StudentRepository;
import com.example.itqan.repository.SystemManagerRepository;
import com.example.itqan.repository.TeacherRepository;
import com.example.itqan.repository.UserRepository;
import com.example.itqan.security.JwtUtil;
import com.example.itqan.service.AuthService;
import com.example.itqan.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://fox-one-promptly.ngrok-free.app"
})
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SystemManagerRepository systemManagerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) throws IOException {
        UserDTO userDTO = authService.login(request);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
       User newUser = authService.register(request);
       return ResponseEntity.ok(newUser);
    }
}