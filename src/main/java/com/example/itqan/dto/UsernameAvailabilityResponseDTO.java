package com.example.itqan.dto;

import java.util.List;

public class UsernameAvailabilityResponseDTO {

    private boolean available;
    private List<String> suggestions;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
}
