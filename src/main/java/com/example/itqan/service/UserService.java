package com.example.itqan.service;

import com.example.itqan.dto.UsernameAvailabilityResponseDTO;
import com.example.itqan.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UsernameAvailabilityResponseDTO checkUsernameAndSuggest(String username) {
        boolean available = !userRepository.existsByUserName(username);

        UsernameAvailabilityResponseDTO responseDTO = new UsernameAvailabilityResponseDTO();

        responseDTO.setAvailable(available);

        if (!available) {
            Set<String> candidates = new HashSet<>();
            Random random = new Random();

            while (candidates.size() < 20) {
                candidates.add(username + random.nextInt(10000));
            }

            List<String> taken = userRepository.findUserNamesByUserNameIn(candidates);
            candidates.removeAll(taken);

            List<String> suggestions = candidates.stream().limit(5).toList();
            responseDTO.setSuggestions(suggestions);
        } else {
            responseDTO.setSuggestions(List.of());
        }

        return responseDTO;
    }


}
