package com.project.userservice.service;

import com.project.userservice.model.Doctors;
import org.springframework.stereotype.Service;

@Service
public interface DoctorService {
    public Doctors registerDoctor(Doctors doctor);
    public Doctors findByEmail(String email);
}
