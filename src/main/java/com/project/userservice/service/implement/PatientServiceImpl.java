package com.project.userservice.service.implement;

import com.project.userservice.dto.ChangePasswordRequest;
import com.project.userservice.model.Patients;
import com.project.userservice.model.Role;
import com.project.userservice.repository.PatientRepository;
import com.project.userservice.service.PasswordService;
import com.project.userservice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PasswordService passwordService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private SendEmail sendEmail;

    @Override
    public Patients findByEmail(String email) {
        return patientRepository.findByPatientEmail(email);
    }

    @Override
    public Patients checkEmail(Patients patients) {
        Patients patient = patientRepository.findByPatientEmail(patients.getPatientEmail());
        if (patient == null) {
            patients.setPatientPassword(passwordEncoder.encode(passwordService.generateRandomPassword()));
            Patients savedPatient = patientRepository.save(patients);
            sendEmail.sendEmail(savedPatient.getPatientName(), savedPatient.getPatientEmail(), savedPatient.getPatientPassword());
            return savedPatient;
        } else {
            return patient;
        }
    }

    @Override
    public Patients registerPatient(Patients patient) {
        patient.setPatientPassword(passwordEncoder.encode(patient.getPatientPassword())); // Mã hóa mật khẩu
        Role patientRole = new Role();
        patientRole.setId(2);
        patient.setRole(patientRole);
        return patientRepository.save(patient);
    }

    @Override
    public Patients updatePatient(Patients patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patients changePassword(ChangePasswordRequest changePasswordRequest) {
        Patients patient = patientRepository.findByPatientEmail(changePasswordRequest.getEmail());
        if (patient == null) return null;
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), patient.getPatientPassword())) return null;
        patient.setPatientPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        return patientRepository.save(patient);
    }
}
