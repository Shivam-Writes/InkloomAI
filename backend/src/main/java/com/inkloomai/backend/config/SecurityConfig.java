package com.inkloomai.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(
            PasswordEncoder encoder,
            @Value("$" + "{app.security.users.admin.username}") String adminUsername,
            @Value("$" + "{app.security.users.admin.password}") String adminPassword,
            @Value("$" + "{app.security.users.brand-manager.username}") String managerUsername,
            @Value("$" + "{app.security.users.brand-manager.password}") String managerPassword,
            @Value("$" + "{app.security.users.content-creator.username}") String creatorUsername,
            @Value("$" + "{app.security.users.content-creator.password}") String creatorPassword,
            @Value("$" + "{app.security.users.reviewer.username}") String reviewerUsername,
            @Value("$" + "{app.security.users.reviewer.password}") String reviewerPassword) {

        requirePassword("RBAC_ADMIN_PASSWORD", adminPassword);
        requirePassword("RBAC_BRAND_MANAGER_PASSWORD", managerPassword);
        requirePassword("RBAC_CONTENT_CREATOR_PASSWORD", creatorPassword);
        requirePassword("RBAC_REVIEWER_PASSWORD", reviewerPassword);

        return new InMemoryUserDetailsManager(
                account(adminUsername, adminPassword, "ADMIN", encoder),
                account(managerUsername, managerPassword, "BRAND_MANAGER", encoder),
                account(creatorUsername, creatorPassword, "CONTENT_CREATOR", encoder),
                account(reviewerUsername, reviewerPassword, "REVIEWER", encoder));
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/health", "/actuator/health", "/actuator/health/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    private UserDetails account(String username, String password, String role, PasswordEncoder encoder) {
        if (username == null || username.isBlank()) {
            throw new IllegalStateException("Configure a username for the " + role + " role.");
        }
        return User.withUsername(username.trim())
                .password(encoder.encode(password))
                .roles(role)
                .build();
    }

    private void requirePassword(String variable, String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalStateException("Set " + variable + " before starting the Inkloom AI backend. Do not use sample passwords in production.");
        }
    }
}
