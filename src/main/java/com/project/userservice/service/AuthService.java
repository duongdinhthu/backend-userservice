package com.project.userservice.service;

import com.project.userservice.model.Staffs; // Hoặc Patients/Doctors nếu cần
import com.project.userservice.repository.DoctorsRepository;
import com.project.userservice.repository.PatientsRepository;
import com.project.userservice.repository.StaffsRepository; // Hoặc tương ứng với Patients/Doctors
import com.project.userservice.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Autowired
    private StaffsRepository staffsRepository; // Hoặc tương ứng với Patients/Doctors

    @Autowired
    private DoctorsRepository doctorsRepository;

    @Autowired
    private PatientsRepository patientsRepository;



    public String login(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Nếu đến đây có nghĩa là xác thực thành công
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("USER"); // Vai trò mặc định nếu không tìm thấy

            return jwtUtils.createToken(email, role); // Tạo JWT với vai trò

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid login attempt");
        }
    }



}
