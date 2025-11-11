package org.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers( "/login", "/register", "/css/**", "/js/**").permitAll()

                        // Applicant endpoints - only APPLICANT role
                        .requestMatchers("/apply").hasAnyRole("APPLICANT", "ADMIN")

                        // Professor endpoints - only PROFESSOR role
                        .requestMatchers("/evaluations").hasAnyRole("PROFESSOR", "ADMIN")

                        // Admin endpoints - only ADMIN role
                        .requestMatchers("/api/applications", "/admin", "/addprofessors", "api/professors").hasRole("ADMIN")

                        .requestMatchers("/").hasAnyRole("ADMIN", "PROFESSOR", "APPLICANT")

                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                ).csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
