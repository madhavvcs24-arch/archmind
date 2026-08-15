package com.archmind.backend.analysis.service;

import org.springframework.stereotype.Service;

import com.archmind.backend.analysis.model.LayerType;

@Service
public class LayerDetector {

    public LayerType detect(String className, String packageName) {

        String lowerClass = className.toLowerCase();
        String lowerPackage = packageName.toLowerCase();

        // Controller
        if (lowerClass.endsWith("controller") ||
                lowerPackage.contains(".controller")) {
            return LayerType.CONTROLLER;
        }

        // Service
        if (lowerClass.endsWith("service") ||
                lowerPackage.contains(".service")) {
            return LayerType.SERVICE;
        }

        // Repository
        if (lowerClass.endsWith("repository") ||
                lowerPackage.contains(".repository")) {
            return LayerType.REPOSITORY;
        }

        // Entity
        if (lowerPackage.contains(".entity")) {
            return LayerType.ENTITY;
        }

        // DTO
        if (lowerClass.endsWith("dto") ||
                lowerPackage.contains(".dto")) {
            return LayerType.DTO;
        }

        // Security
        if (lowerPackage.contains(".security") ||
                lowerClass.contains("security") ||
                lowerClass.contains("jwt")) {
            return LayerType.SECURITY;
        }

        // Configuration
        if (lowerClass.endsWith("config") ||
                lowerPackage.contains(".config")) {
            return LayerType.CONFIGURATION;
        }

        // Utility
        if (lowerPackage.contains(".util") ||
                lowerPackage.contains(".utils")) {
            return LayerType.UTILITY;
        }

        return LayerType.UNKNOWN;
    }
}