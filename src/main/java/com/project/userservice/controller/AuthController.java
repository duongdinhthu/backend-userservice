package com.project.userservice.controller;

import com.project.userservice.dto.*;
import com.project.userservice.model.Doctors;
import com.project.userservice.model.Patients;
import com.project.userservice.model.Role;
import com.project.userservice.model.Staffs;
import com.project.userservice.service.AuthService;
import com.project.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register/patient")
    public ResponseEntity<Map<String, String>> registerPatient(@RequestBody PatientRegistrationDto registrationDto) {
        Patients patient = new Patients();
        patient.setPatientName(registrationDto.getPatientName());
        patient.setPatientEmail(registrationDto.getPatientEmail());
        patient.setPatientPhone(registrationDto.getPatientPhone());
        patient.setPatientPassword(passwordEncoder.encode(registrationDto.getPatientPassword())); // Mã hóa mật khẩu

        // Gán vai trò cho bệnh nhân
        Role patientRole = new Role();
        patientRole.setId(2); // ID vai trò cho bệnh nhân
        patient.setRole(patientRole);

        userService.registerPatient(patient);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Patient registered successfully!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<Map<String, String>> registerDoctor(@RequestBody DoctorRegistrationDto registrationDto) {
        Doctors doctor = new Doctors();
        doctor.setDoctorName(registrationDto.getDoctorName());
        doctor.setDoctorEmail(registrationDto.getDoctorEmail());
        doctor.setDoctorPhone(registrationDto.getDoctorPhone());
        doctor.setDoctorPassword(passwordEncoder.encode(registrationDto.getDoctorPassword())); // Mã hóa mật khẩu

        // Gán vai trò cho bác sĩ
        Role doctorRole = new Role();
        doctorRole.setId(1); // ID vai trò cho bác sĩ
        doctor.setRole(doctorRole);

        userService.registerDoctor(doctor);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Doctor registered successfully!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/staff")
    public ResponseEntity<Map<String, String>> registerStaff(@RequestBody StaffRegistrationDto registrationDto) {
        Staffs staff = new Staffs();
        staff.setStaffName(registrationDto.getStaffName());
        staff.setStaffPhone(registrationDto.getStaffPhone());
        staff.setStaffAddress(registrationDto.getStaffAddress());
        staff.setStaffType(registrationDto.getStaffType());
        staff.setStaffEmail(registrationDto.getStaffEmail());
        staff.setStaffPassword(passwordEncoder.encode(registrationDto.getStaffPassword())); // Mã hóa mật khẩu
        staff.setStaffStatus(registrationDto.getStaffStatus()); // Nếu cần thiết

        // Gán vai trò cho nhân viên
        Role staffRole = new Role();
        staffRole.setId(registrationDto.getRoleId()); // ID vai trò cho nhân viên
        staff.setRole(staffRole);

        userService.registerStaff(staff);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Staff registered successfully!");
        return ResponseEntity.ok(response);
    }



    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest.getEmail()+" "+loginRequest.getPassword());
        String token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/staff/overview")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<String> getStaffOverview() {
        return ResponseEntity.ok("This is the staff overview, accessible only to staff members.");
    }

    @GetMapping("/admin/overview")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAdminOverview() {
        return ResponseEntity.ok("This is the admin overview, accessible only to staff members.");
    }

    // Ví dụ API yêu cầu vai trò DOCTOR
    @GetMapping("/doctor/overview")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> getDoctorOverview() {
        System.out.println("gọi api thanh cong");
        return ResponseEntity.ok("This is the doctor overview, accessible only to doctors.");
    }

    // Ví dụ API yêu cầu vai trò PATIENT
    @GetMapping("/patient/overview")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> getPatientOverview() {
        return ResponseEntity.ok("This is the patient overview, accessible only to patients.");
    }
}
