package com.mpt.dentalclinic.config;

import com.mpt.dentalclinic.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserDetailsServiceImpl userDetailsServiceImpl) {
        return userDetailsServiceImpl;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http, DaoAuthenticationProvider authenticationProvider) throws Exception {
    http
        .authenticationProvider(authenticationProvider)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/login", "/registration", "/css/**", "/js/**").permitAll()
            
            .requestMatchers("/admin/**", "/userList/**", "/roleList/**", 
                           "/paymentMethodList/**", "/statusOrderList/**").hasAuthority("ROLE_ADMIN")
            
            .requestMatchers(
                "/serviceList/**",
                "/categoryList/**",
                "/paymentList/**"
            ).hasAnyAuthority("ROLE_DOCTOR", "ROLE_ADMIN", "ROLE_PATIENT")

            .requestMatchers("/appointmentList/**")
                .hasAnyAuthority("ROLE_PATIENT", "ROLE_DOCTOR", "ROLE_ADMIN")

            .requestMatchers("/reviewList/**")
                .hasAnyAuthority("ROLE_PATIENT", "ROLE_ADMIN")
            
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/")
            .failureUrl("/login?error=true")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        )
        .exceptionHandling(handling -> handling
            .accessDeniedPage("/access-denied")
        );

    return http.build();
}
}