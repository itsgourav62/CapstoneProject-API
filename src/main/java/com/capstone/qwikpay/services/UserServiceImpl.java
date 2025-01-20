package com.capstone.qwikpay.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.qwikpay.entities.UserEntity;
import com.capstone.qwikpay.exceptions.UserNotFoundException;
import com.capstone.qwikpay.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);  // Initialize logger

    @Autowired
    private UserRepository repo;

    // Add new user
    @Override
    public UserEntity addNewUser(UserEntity user) {
        logger.info("Adding a new user with username: {}", user.getUsername());
        try {
            UserEntity savedUser = repo.save(user);
            logger.info("User with username: {} added successfully with ID: {}", user.getUsername(), savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            logger.error("Error while adding user with username: {}", user.getUsername(), e);
            throw e;
        }
    }

    // Get user by ID with exception handling
    @Override
    public UserEntity getUser(Integer userId) throws UserNotFoundException {
        logger.info("Fetching user with ID: {}", userId);
        Optional<UserEntity> user = repo.findById(userId);
        if (user.isPresent()) {
            logger.info("User with ID: {} retrieved successfully", userId);
            return user.get();
        } else {
            logger.error("User with ID: {} not found", userId);
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
    }

    // Update user
    @Override
    public UserEntity updateUser(UserEntity user) throws UserNotFoundException {
        logger.info("Updating user with ID: {}", user.getId());
        if (repo.existsById(user.getId())) {
            try {
                UserEntity updatedUser = repo.save(user);
                logger.info("User with ID: {} updated successfully", user.getId());
                return updatedUser;
            } catch (Exception e) {
                logger.error("Error while updating user with ID: {}", user.getId(), e);
                throw e;
            }
        } else {
            logger.error("User with ID: {} not found for update", user.getId());
            throw new UserNotFoundException("User with ID " + user.getId() + " not found");
        }
    }

    // Delete user by ID
    @Override
    public String deleteUser(Integer userId) throws UserNotFoundException {
        logger.info("Deleting user with ID: {}", userId);
        if (repo.existsById(userId)) {
            try {
                repo.deleteById(userId);
                logger.info("User with ID: {} deleted successfully", userId);
                return "User with ID " + userId + " deleted successfully";
            } catch (Exception e) {
                logger.error("Error while deleting user with ID: {}", userId, e);
                throw e;
            }
        } else {
            logger.error("User with ID: {} not found for deletion", userId);
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
    }

    // Get all users
    @Override
    public List<UserEntity> getUsers() {
        logger.info("Fetching all users");
        List<UserEntity> users = (List<UserEntity>) repo.findAll();
        logger.info("Total number of users retrieved: {}", users.size());
        return users;
    }
}
