// src/main/java/com/hmec/admission_portal/config/SecurityConfig.java
package com.hmec.admission_portal.config;

import com.hmec.admission_portal.repository.AdminRepository;
import com.hmec.admission_portal.security.JwtAuthFilter;
import com.hmec.admission_portal.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AdminRepository adminRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var jwtFilter = new JwtAuthFilter(jwtUtil, adminRepository);

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(req -> {
                    var c = new CorsConfiguration();
                    c.setAllowedOrigins(List.of("*")); // adjust in production
                    c.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
                    c.setAllowedHeaders(List.of("*"));
                    c.setExposedHeaders(List.of("Authorization"));
                    return c;
                }))
                .authorizeHttpRequests(auth -> auth
                        // Public admin auth endpoints
                        .requestMatchers(HttpMethod.POST, "/api/admin/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/admin/auth/login").permitAll()

                        // Public APIs
                        .requestMatchers("/api/enquiries/**", "/api/admissions/**").permitAll()

                        // Protected admin endpoints
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
