package com.example.itqan.service;

import com.example.itqan.dto.AuthRequest;
import com.example.itqan.dto.RegisterRequest;
import com.example.itqan.dto.UserDTO;
import com.example.itqan.exceptions.ItemExistsException;
import com.example.itqan.exceptions.ResourceNotFoundException;
import com.example.itqan.model.*;
import com.example.itqan.repository.StudentRepository;
import com.example.itqan.repository.SystemManagerRepository;
import com.example.itqan.repository.TeacherRepository;
import com.example.itqan.repository.UserRepository;
import com.example.itqan.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class AuthService {
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

    public UserDTO login(AuthRequest request) throws IOException {
        Optional<User> userOptional = userRepository.findByEmail(request.getIdentifier());
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByUserName(request.getIdentifier());
        }
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = userOptional.get();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new IOException("Incorrect password");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new UserDTO(token,user.getRole(), user.getName(), user.getUsername(),user.getId(),user.getGender());
    }

    public User register(RegisterRequest request){

        if (userRepository.findByEmail(request.email).isPresent()) {
            throw new ItemExistsException("Email already in use");
        }
        User newUser;
        if (request.role == Role.MANAGER) {
            SystemManager manager = new SystemManager();
            manager.setUserName(request.userName);
            manager.setName(request.name);
            manager.setEmail(request.email);
            manager.setPhoneNumber(request.phoneNumber);
            manager.setRole(Role.MANAGER);
            manager.setPassword(passwordEncoder.encode(request.password));
            manager.setGender(request.gender);
            newUser = systemManagerRepository.save(manager);
        } else if (request.role == Role.TEACHER){
            Teacher teacher = new Teacher();
            teacher.setUserName(request.userName);
            teacher.setName(request.name);
            teacher.setEmail(request.email);
            teacher.setPhoneNumber(request.phoneNumber);
            teacher.setRole(Role.TEACHER);
            teacher.setPassword(passwordEncoder.encode(request.password));
            teacher.setGender(request.gender);
            newUser = teacherRepository.save(teacher);
        }
        else{
            Student student = new Student();
            student.setUserName(request.userName);
            student.setName(request.name);
            student.setEmail(request.email);
            student.setPhoneNumber(request.phoneNumber);
            student.setRole(Role.STUDENT);
            student.setPassword(passwordEncoder.encode(request.password));
            student.setGender(request.gender);
            newUser = studentRepository.save(student);
        }
        return newUser;
    }
}
