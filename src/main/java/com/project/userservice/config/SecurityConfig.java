package com.project.userservice.config;

import com.project.userservice.security.JwtRequestFilter;
import com.project.userservice.service.implement.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
                                .requestMatchers("/api/userservice/login"
                                        , "/api/userservice/patients/**"
                                        , "/api/userservice/doctors/**"
                                        , "/api/userservice/staffs/**").permitAll()
                                .requestMatchers("/api/userservice/patient/").hasRole("PATIENT")
                                .requestMatchers("/api/userservice/doctor/").hasRole("DOCTOR")
                                .requestMatchers("/api/userservice/staff/").hasRole("STAFF")
                                .requestMatchers("/api/userservice/admin/").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // Tắt CSRF nếu không cần
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // Thêm bộ lọc JWT vào chuỗi bộ lọc
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOrigins(java.util.Collections.singletonList("http://localhost:8080")); // Cho phép tất cả các nguồn
                    corsConfig.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // Cho phép tất cả các phương thức
                    corsConfig.setAllowedHeaders(java.util.Arrays.asList("*")); // Cho phép tất cả các header
                    corsConfig.setAllowCredentials(true); // Cho phép gửi cookie
                    return corsConfig;
                })) // Cấu hình CORS chấp nhận tất cả
                .authenticationManager(authManager(http)); // Thêm AuthenticationManager vào cấu hình

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
