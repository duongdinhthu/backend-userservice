package com.project.userservice.repository;

import com.project.userservice.model.Patients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientsRepository extends JpaRepository<Patients, Long> {
    Patients findByPatientEmail(String email);
}
