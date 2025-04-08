package com.example.itqan.security;

import com.example.itqan.model.User;
import com.example.itqan.repository.SystemManagerRepository;
import com.example.itqan.repository.TeacherRepository;
import com.example.itqan.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final TeacherRepository teacherRepository;
    private final SystemManagerRepository systemManagerRepository;

    public MyUserDetailsService(TeacherRepository teacherRepo, SystemManagerRepository managerRepo) {
        this.teacherRepository = teacherRepo;
        this.systemManagerRepository = managerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return teacherRepository.findByEmail(email)
                .map(t -> (UserDetails) t)
                .or(() -> systemManagerRepository.findByEmail(email)
                        .map(m -> (UserDetails) m))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
