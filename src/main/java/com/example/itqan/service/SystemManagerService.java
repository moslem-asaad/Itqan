package com.example.itqan.service;

import com.example.itqan.model.SystemManager;
import com.example.itqan.repository.SystemManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemManagerService {

    private final SystemManagerRepository systemManagerRepository;

    public SystemManagerService(SystemManagerRepository systemManagerRepository) {
        this.systemManagerRepository = systemManagerRepository;
    }

    public List<SystemManager> getAllManagers() {
        return systemManagerRepository.findAll();
    }
}
