package com.capstone.qwikpay.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

    public final static String[] PUBLIC_REQUEST_MATCHERS = { "/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**" };

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

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
            // Authorize requests
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/user/delete/**").hasRole("ADMIN")
                .requestMatchers("/api/user/update/**").hasRole("ADMIN")
                .requestMatchers("/api/user/new/**").hasRole("ADMIN")
                .requestMatchers("/api/user/get/**").permitAll()
                .requestMatchers("/api/user/users").hasAnyRole("USER", "ADMIN")
                .requestMatchers(PUBLIC_REQUEST_MATCHERS).permitAll()

                // Bill API access control
                .requestMatchers("/api/bills/update/{billId}/**").hasRole("ADMIN")
                .requestMatchers("/api/bills/new/**").hasRole("ADMIN")
//                .requestMatchers("/api/bills/new/**").permitAll() //TODO change the persmission after fixing the issue 
                .requestMatchers("/api/bills/{status}/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/bills/retrieveBillById/{billId}/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/bills/user/{userId}").hasRole("ADMIN")
                .requestMatchers("/api/bills/retrievAll").hasAnyRole("USER","ADMIN")
                .requestMatchers("/api/bills/delete/{billId}").hasRole("ADMIN")

                // Payment API access control
                .requestMatchers("/api/payments/process").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/payments/status/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/payments/{id}").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/payments").hasAnyRole("USER", "ADMIN")
            )
            // Disable CSRF for simplicity (not recommended for production)
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
