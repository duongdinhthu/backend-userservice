package com.project.userservice.repository;

import com.project.userservice.model.Doctors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorsRepository extends JpaRepository<Doctors, Long> {
    Doctors findByDoctorEmail(String email);
}
