package org.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/apply", "/login", "/register", "/css/**", "/js/**").permitAll()
                        // Allow read-only access to professor listing so applicants can select professors
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/professors", "/professors/**").permitAll()
                        // Admin can manage professors (create/update/delete)
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/professors", "/professors/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/professors/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/professors/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/professors/**").hasRole("ADMIN")
                        // Allow public application submissions to Spring Data REST repository
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/applicationFiles", "/applicationFiles/**").permitAll()

                        // Applicant endpoints - only APPLICANT role
                        .requestMatchers("/apply").hasAnyRole("APPLICANT", "ADMIN")

                        // Professor portal
                        .requestMatchers("/evaluations").hasAnyRole("PROFESSOR", "ADMIN")

                        // API access:
                        // Professors and admins can GET applications and update status via PUT
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/applications", "/api/applications/**").hasAnyRole("PROFESSOR","ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/applications/**").hasAnyRole("PROFESSOR","ADMIN")

                        // Admin-only areas and other API writes
                        .requestMatchers("/admin", "/addprofessors").hasRole("ADMIN")
                        .requestMatchers("/api/**").hasRole("ADMIN")

                        // Default homepage requires a logged-in session (any authenticated user)
                        .requestMatchers("/").authenticated()

                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                ).csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new RoleBasedAuthenticationSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
