package com.capstone.qwikpay.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.capstone.qwikpay.entities.UserEntity;
import com.capstone.qwikpay.repositories.UserRepository;
import com.capstone.qwikpay.security.jwt.JwtUtils;
import com.capstone.qwikpay.security.payload.request.LoginRequest;
import com.capstone.qwikpay.security.payload.request.SignUpRequest;
import com.capstone.qwikpay.services.UserDetailsImpl;

class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }



    @Test
    void testRegisterUser_UsernameAlreadyExists() throws Exception {
        // Creating the request body JSON
        String requestBody = "{\"username\":\"user1\", \"email\":\"user1@example.com\", \"password\":\"password\", " +
                "\"mobile\":\"1234567890\", \"address\":\"1234 Elm St\", \"gender\":\"M\", \"role\":[\"user\"]}";

        // Create a SignUpRequest for testing
        SignUpRequest signUpRequest = new SignUpRequest("user1", "user1@example.com", Set.of("user"), "password", "1234567890", "1234 Elm St", "M");

        // Mock the userRepository to return true when checking if the username exists
        when(userRepository.existsByUsername("user1")).thenReturn(true);

        // Perform the test
        mockMvc.perform(post("/api/auth/signup")
                .contentType("application/json")
                .content(requestBody))   // Use the corrected requestBody here
                .andExpect(status().isBadRequest())   // Expect a BadRequest status
                .andExpect(jsonPath("$.message").value("Error: Username is already taken!"));   // Expect the error message
    }


    @Test
    void testForgotPassword_Success() throws Exception {
        String email = "user1@example.com";
        String newPassword = "newPassword";

        UserEntity user = new UserEntity("user1", email, "encodedPassword");
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        mockMvc.perform(post("/api/auth/forgot-password")
                .contentType("application/json")
                .content("{\"email\":\"" + email + "\", \"password\":\"" + newPassword + "\", \"confirmPassword\":\"" + newPassword + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password updated successfully!"));
    }




}