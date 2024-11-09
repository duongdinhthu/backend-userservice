package com.project.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Lấy URL của request
        String requestURI = request.getRequestURI();

        // Bỏ qua kiểm tra JWT cho các API không cần xác thực
        if (requestURI.startsWith("/api/userservice/login") ||
                requestURI.startsWith("/api/userservice/patients/register") ||
                requestURI.startsWith("/api/userservice/doctors/register") ||
                requestURI.startsWith("/api/userservice/staffs/register")) {
            // Nếu là các API này, không cần kiểm tra JWT, chỉ cần tiếp tục với chuỗi bộ lọc
            chain.doFilter(request, response);
            System.out.println("api khong yeu cau jwt");
            return;
        }

        final String authorizationHeader = request.getHeader("authorization");

        String username = null;
        String jwt = null;

        // Kiểm tra nếu header chứa Bearer Token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);  // Lấy JWT từ header
            username = jwtUtils.getUsername(jwt);  // Lấy username từ JWT
        }

        // Nếu username không null và không có authentication trong context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Kiểm tra token hợp lệ
            if (jwtUtils.validateToken(jwt)) {
                List<GrantedAuthority> authorities = jwtUtils.getAuthorities(jwt); // Lấy authorities từ JWT

                // Tạo đối tượng Authentication và thiết lập nó vào SecurityContext
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Tiếp tục xử lý yêu cầu
        chain.doFilter(request, response);
    }
}
