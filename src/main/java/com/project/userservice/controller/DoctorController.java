package com.project.userservice.controller;

import com.project.userservice.dto.DoctorRegistrationDto;
import com.project.userservice.model.Doctors;
import com.project.userservice.model.Role;
import com.project.userservice.service.DoctorService;
import com.project.userservice.service.implement.DoctorServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/userservice/doctors")
public class DoctorController {

    @Autowired
    DoctorService doctorService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<Doctors> registerDoctor(@RequestBody DoctorRegistrationDto registrationDto) {
        ModelMapper modelMapper = new ModelMapper();
        Doctors doctor = modelMapper.map(registrationDto, Doctors.class);
        doctorService.registerDoctor(doctor);
        return ResponseEntity.ok(doctorService.registerDoctor(doctor));
    }
}
