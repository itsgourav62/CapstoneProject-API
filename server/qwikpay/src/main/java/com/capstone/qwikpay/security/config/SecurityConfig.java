package com.capstone.qwikpay.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.capstone.qwikpay.security.jwt.AuthEntryPointJwt;
import com.capstone.qwikpay.security.jwt.AuthTokenFilter;
import com.capstone.qwikpay.services.UserDetailsServiceImpl;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    public final static String[] PUBLIC_REQUEST_MATCHERS = {
        "/api/auth/**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/h2-console/**" // Allow access to H2 Console
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF
            .authorizeHttpRequests(authorize -> 
                authorize.requestMatchers(PUBLIC_REQUEST_MATCHERS).permitAll() // Allow public requests
                    // Bill API access control
                    .requestMatchers("/api/bills/**").hasAnyRole("USER", "ADMIN") // All bill endpoints restricted to authenticated users
                    .requestMatchers("/api/bills/user/**").hasRole("USER")       // Bill access by user ID restricted to ROLE_USER
                    .requestMatchers("/api/bills/status/**").hasAnyRole("USER", "ADMIN") // Bill access by status for both roles
                    .requestMatchers("/api/bills/{id}").hasAnyRole("USER", "ADMIN")     // Single bill retrieval allowed for both roles
                    .requestMatchers("/api/bills").hasRole("ADMIN") // Only admins can create or modify bills
                    
                    
                    //Payment API access control
                    .requestMatchers("/api/payments/process").hasAnyRole("USER", "ADMIN") // Process a payment can be done by both USER and ADMIN
                    .requestMatchers("/api/payments/validate/**").hasAnyRole("USER", "ADMIN") // Validate payment by ID
                    .requestMatchers("/api/payments/{id}").hasAnyRole("USER", "ADMIN") // Retrieve payment by ID
                    .requestMatchers("/api/payments").hasRole("ADMIN") // Admin can view all payments
                    
            )
            .headers(headers -> 
                headers.frameOptions(frameOptions -> frameOptions.sameOrigin()) // Enable H2 Console access
            )
            .exceptionHandling(exception -> 
                exception.authenticationEntryPoint(unauthorizedHandler)
            )
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
