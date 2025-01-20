package com.capstone.qwikpay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.qwikpay.entities.UserEntity;
import com.capstone.qwikpay.exceptions.UserNotFoundException;
import com.capstone.qwikpay.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;

    // Get user by Id
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/{id}")
    public UserEntity getUser(@PathVariable("id") Integer userId) throws UserNotFoundException {
        logger.info("Fetching user with ID: {}", userId);
        
        try {
            UserEntity user = service.getUser(userId);
            logger.info("User found with ID: {}", userId);
            return user;
        } catch (UserNotFoundException e) {
            logger.error("User with ID {} not found", userId);
            throw e;
        }
    }

    // Update user by Id
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public UserEntity updateUser(@RequestBody UserEntity user) throws UserNotFoundException {
        logger.info("Updating user with ID: {}", user.getId());

        try {
            UserEntity updatedUser = service.updateUser(user);
            logger.info("User with ID {} updated successfully", user.getId());
            return updatedUser;
        } catch (UserNotFoundException e) {
            logger.error("User with ID {} not found for update", user.getId());
            throw e;
        }
    }

    // Delete user by id
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer userId) throws UserNotFoundException {
        logger.info("Deleting user with ID: {}", userId);

        try {
            String response = service.deleteUser(userId);
            logger.info("User with ID {} deleted successfully", userId);
            return response;
        } catch (UserNotFoundException e) {
            logger.error("User with ID {} not found for deletion", userId);
            throw e;
        }
    }

    // Get all users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<UserEntity> getUsers() {
        logger.info("Fetching all users");
        
        List<UserEntity> users = service.getUsers();
        logger.info("Retrieved {} users", users.size());
        return users;
    }
}
