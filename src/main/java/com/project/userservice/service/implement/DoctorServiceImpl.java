package com.project.userservice.service.implement;

import com.project.userservice.model.Doctors;
import com.project.userservice.model.Role;
import com.project.userservice.repository.DoctorsRepository;
import com.project.userservice.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    DoctorsRepository doctorsRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    Doctors doctors =new Doctors()   ;



    @Override
    public Doctors registerDoctor(Doctors doctor) {
        doctors.setDoctorName(doctor.getDoctorName());
        doctors.setDoctorEmail(doctor.getDoctorEmail());
        doctors.setDoctorPhone(doctor.getDoctorPhone());
        doctors.setDoctorPassword(passwordEncoder.encode(doctor.getDoctorPassword())); // Mã hóa mật khẩu
        Role doctorRole = new Role();
        doctorRole.setId(1);
        doctors.setRole(doctorRole);
        return doctorsRepository.save(doctors);
    }

    @Override
    public Doctors findByEmail(String email) {

        return doctorsRepository.findByDoctorEmail(email);
    }
}
