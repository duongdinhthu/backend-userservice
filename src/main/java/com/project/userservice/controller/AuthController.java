package com.project.userservice.controller;

import com.project.userservice.dto.*;
import com.project.userservice.service.AuthService;
import com.project.userservice.service.implement.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/userservice")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest.getEmail()+" "+loginRequest.getPassword());
        String token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/staff/overview")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Map<String, String>> getStaffOverview() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is the staff overview, accessible only to staff members.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/overview")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> getAdminOverview() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is the admin overview, accessible only to admin members.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/overview")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, String>> getDoctorOverview() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is the doctor overview, accessible only to doctors.");
        System.out.println("API called successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/overview")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Map<String, String>> getPatientOverview() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is the patient overview, accessible only to patients.");
        return ResponseEntity.ok(response);
    }


}
