package com.project.userservice.service;

import com.project.userservice.model.*;
import com.project.userservice.repository.PatientsRepository;
import com.project.userservice.repository.DoctorsRepository;
import com.project.userservice.repository.StaffsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PatientsRepository patientsRepository;

    @Autowired
    private DoctorsRepository doctorsRepository;

    @Autowired
    private StaffsRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Tìm kiếm trong bảng Patients
        Patients patient = patientsRepository.findByPatientEmail(email);
        if (patient != null) {
            Role role = patient.getRole(); // Lấy đối tượng Role của staff
            String roleName = role.getName().toUpperCase();
            System.out.println("Patient found: " + patient.getPatientEmail() + ", Role: " + roleName);
            return createUserDetails(patient.getPatientEmail(), patient.getPatientPassword(), roleName);
        }

        // Tìm kiếm trong bảng Doctors
        Doctors doctor = doctorsRepository.findByDoctorEmail(email);
        if (doctor != null) {
            Role role = doctor.getRole(); // Lấy đối tượng Role của staff
            String roleName = role.getName().toUpperCase(); // Chuyển tên role thành dạng ROLE_
            System.out.println("Doctor found: " + doctor.getDoctorEmail()+ ", Role: " + roleName);
            return createUserDetails(doctor.getDoctorEmail(), doctor.getDoctorPassword(), roleName);
        }

        // Tìm kiếm trong bảng Staff
        Staffs staff = staffRepository.findByStaffEmail(email);
        if (staff != null) {
            Role role = staff.getRole();
            String roleName = role.getName().toUpperCase(); // Chuyển tên role thành dạng ROLE_
            System.out.println("Role object: " + role); // Kiểm tra giá trị của đối tượng Role
            System.out.println("Role name: " + roleName); // Kiểm tra tên vai trò
            System.out.println("Staff found: " + staff.getStaffEmail() + ", Role: " + roleName);
            return createUserDetails(staff.getStaffEmail(), staff.getStaffPassword(), roleName);
        }


        throw new UsernameNotFoundException("User not found with email: " + email);
    }


    private UserDetails createUserDetails(String email, String password, String role) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        System.out.println(authorities.toString());
        return org.springframework.security.core.userdetails.User.withUsername(email)
                .password(password)
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
