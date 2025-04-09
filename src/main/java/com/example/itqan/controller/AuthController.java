package com.example.itqan.controller;

import com.example.itqan.dto.RegisterRequest;
import com.example.itqan.model.Role;
import com.example.itqan.model.SystemManager;
import com.example.itqan.model.Teacher;
import com.example.itqan.model.User;
import com.example.itqan.repository.SystemManagerRepository;
import com.example.itqan.repository.TeacherRepository;
import com.example.itqan.repository.UserRepository;
import com.example.itqan.security.JwtUtil;
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
    private SystemManagerRepository systemManagerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
        }

        User user = userOptional.get();
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, user.getRole(),user.getName()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.email).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        User newUser;
        if (request.role == Role.MANAGER) {
            SystemManager manager = new SystemManager();
            manager.setName(request.name);
            manager.setEmail(request.email);
            manager.setPhoneNumber(request.phoneNumber);
            manager.setRole(Role.MANAGER);
            manager.setPassword(passwordEncoder.encode(request.password));
            newUser = systemManagerRepository.save(manager);
        } else {
            Teacher teacher = new Teacher();
            teacher.setName(request.name);
            teacher.setEmail(request.email);
            teacher.setPhoneNumber(request.phoneNumber);
            teacher.setRole(Role.TEACHER);
            teacher.setPassword(passwordEncoder.encode(request.password));
            newUser = teacherRepository.save(teacher);
        }
        return ResponseEntity.ok(newUser);
    }


    public static class AuthRequest {
        public String email;
        public String password;

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class AuthResponse {
        public String token;
        public Role role;

        public String userName;

        public AuthResponse(String token,Role role, String userName) {
            this.token = token;
            this.role = role;
            this.userName = userName;
        }
    }
}