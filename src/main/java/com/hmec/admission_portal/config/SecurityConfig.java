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
                    c.setAllowedOrigins(List.of("*")); // tighten in prod
                    c.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
                    c.setAllowedHeaders(List.of("*"));
                    c.setExposedHeaders(List.of("Authorization"));
                    return c;
                }))
                .authorizeHttpRequests(auth -> auth
                        // Allow static frontend resources
                        .requestMatchers("/", "/index.html", "/success.html", "/logo.jpg",
                                "/admin/**", "/enquiry/**", "/admission/**", "/payment/**").permitAll()

                        // Public admin auth endpoints
                        .requestMatchers(HttpMethod.POST, "/api/admin/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/admin/auth/login").permitAll()

                        // Public APIs
                        .requestMatchers("/api/enquiries/**", "/api/admissions/images/**").permitAll()

                        // Protected admin APIs
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Everything else free (frontend assets)
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
