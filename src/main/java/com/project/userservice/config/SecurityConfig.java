package com.project.userservice.config;

import com.project.userservice.security.JwtRequestFilter; // Import bộ lọc JWT
import com.project.userservice.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Đảm bảo có dòng này
public class SecurityConfig {

    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter; // Thêm bộ lọc JWT

    public SecurityConfig(DataSource dataSource, CustomUserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter; // Khởi tạo bộ lọc JWT
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/auth/login", "/api/auth/register/**").permitAll()
                                .requestMatchers("/api/auth/patient/**").hasRole("PATIENT")
                                .requestMatchers("/api/auth/doctor/**").hasRole("DOCTOR")
                                .requestMatchers("/api/auth/staff/**").hasAnyRole("STAFF")
                                .requestMatchers("/api/auth/admin/**").hasAnyRole("ADMIN")

                                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // Tắt CSRF nếu không cần
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Thêm bộ lọc JWT vào chuỗi bộ lọc
        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService) // Sử dụng CustomUserDetailsService
                .passwordEncoder(passwordEncoder()); // Mã hóa mật khẩu
        return authenticationManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
