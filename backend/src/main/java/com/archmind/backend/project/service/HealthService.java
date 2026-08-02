package com.archmind.backend.project.service;

import org.springframework.stereotype.Service;

import com.archmind.backend.common.response.ApiResponse;

@Service
public class HealthService {

    public ApiResponse getHealth() {
        return new ApiResponse(
                true,
                "Backend is running",
                null
        );
    }
}