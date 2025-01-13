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
                    // Secure TransactionController endpoints
                    .requestMatchers("/api/transactions/{transactionId}").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/api/transactions/transactions/{paymentId}").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/api/transactions/createTransaction").hasRole("ADMIN")
                    // Other secured endpoints
                    .requestMatchers("/greet").hasRole("USER")
                    .requestMatchers("/admingreet").hasRole("ADMIN")
                    .requestMatchers("/api/customer/delete/**").hasRole("ADMIN")
                    .requestMatchers("/api/customer/update").hasRole("ADMIN")
                    .requestMatchers("/api/customer/new").hasRole("ADMIN")
                    .requestMatchers("/api/customer/get/**").permitAll()
                    .requestMatchers("/api/customer/customers").hasAnyRole("USER", "ADMIN")
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
