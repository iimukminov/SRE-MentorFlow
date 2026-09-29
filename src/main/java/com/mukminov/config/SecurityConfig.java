package com.mukminov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/index", "/login", "/register", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/roadmaps", "/api/v1/roadmaps/**", "/api/v1/steps/**").hasRole("MENTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/roadmaps/**", "/api/v1/steps/**").hasRole("MENTOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/steps/**", "/api/v1/consultations/**").hasRole("MENTOR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/board/**", "/api/v1/connections/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/board/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/connections/**").authenticated()

                        .requestMatchers("/roadmap", "/roadmap/**", "/board", "/notifications").hasAnyRole("MENTEE", "MENTOR")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/roadmap", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}