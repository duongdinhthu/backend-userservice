package com.project.userservice.service;

import com.project.userservice.model.Doctors;
import com.project.userservice.model.Patients;
import com.project.userservice.model.Staffs;
import com.project.userservice.repository.DoctorsRepository;
import com.project.userservice.repository.PatientsRepository;
import com.project.userservice.repository.StaffsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private PatientsRepository patientsRepository;

    @Autowired
    private DoctorsRepository doctorsRepository;

    @Autowired
    private StaffsRepository staffsRepository;

    public Patients registerPatient(Patients patient) {
        return patientsRepository.save(patient);
    }

    public Doctors registerDoctor(Doctors doctor) {
        return doctorsRepository.save(doctor);
    }

    public Staffs registerStaff(Staffs staff) {
        return staffsRepository.save(staff);
    }
}
