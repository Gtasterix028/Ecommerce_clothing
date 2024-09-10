package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.exception.EmailAlreadyExistsException;
import com.gtasterix.E_Commerce.exception.ResourceNotFoundException;
import com.gtasterix.E_Commerce.exception.UserNotFoundException;
import com.gtasterix.E_Commerce.model.User;
import com.gtasterix.E_Commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String MOBILE_REGEX = "^[6-9][0-9]{9}$";

    public User createUser(User user) {
        validateEmail(user.getEmail());
        validateMobileNumber(user.getMobileNumber());

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String username = generateUniqueUsername();
        user.setUsername(username);

        return userRepository.save(user);
    }

    public User updateUserById(UUID userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validateEmail(user.getEmail());
        validateMobileNumber(user.getMobileNumber());

        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setMobileNumber(user.getMobileNumber());
        existingUser.setAddress(user.getAddress());
        existingUser.setRole(user.getRole());

        return userRepository.save(existingUser);
    }

    public User updateUserByUsername(String username, User user) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validateEmail(user.getEmail());
        validateMobileNumber(user.getMobileNumber());

        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setMobileNumber(user.getMobileNumber());
        existingUser.setAddress(user.getAddress());
        existingUser.setRole(user.getRole());

        return userRepository.save(existingUser);
    }

    public User updateUserByEmail(String email, User user) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validateEmail(user.getEmail());
        validateMobileNumber(user.getMobileNumber());

        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setMobileNumber(user.getMobileNumber());
        existingUser.setAddress(user.getAddress());
        existingUser.setRole(user.getRole());

        return userRepository.save(existingUser);
    }

    public User patchUserById(UUID userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getFullName() != null) existingUser.setFullName(user.getFullName());
        if (user.getEmail() != null) {
            validateEmail(user.getEmail());
            existingUser.setEmail(user.getEmail());
        }
        if (user.getMobileNumber() != null) {
            validateMobileNumber(user.getMobileNumber());
            existingUser.setMobileNumber(user.getMobileNumber());
        }
        if (user.getAddress() != null) existingUser.setAddress(user.getAddress());
        if (user.getRole() != null) existingUser.setRole(user.getRole());

        return userRepository.save(existingUser);
    }

    public User patchUserByUsername(String username, User user) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getFullName() != null) existingUser.setFullName(user.getFullName());
        if (user.getEmail() != null) {
            validateEmail(user.getEmail());
            existingUser.setEmail(user.getEmail());
        }
        if (user.getMobileNumber() != null) {
            validateMobileNumber(user.getMobileNumber());
            existingUser.setMobileNumber(user.getMobileNumber());
        }
        if (user.getAddress() != null) existingUser.setAddress(user.getAddress());
        if (user.getRole() != null) existingUser.setRole(user.getRole());

        return userRepository.save(existingUser);
    }

    public User patchUserByEmail(String email, User user) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getFullName() != null) existingUser.setFullName(user.getFullName());
        if (user.getEmail() != null) {
            validateEmail(user.getEmail());
            existingUser.setEmail(user.getEmail());
        }
        if (user.getMobileNumber() != null) {
            validateMobileNumber(user.getMobileNumber());
            existingUser.setMobileNumber(user.getMobileNumber());
        }
        if (user.getAddress() != null) existingUser.setAddress(user.getAddress());
        if (user.getRole() != null) existingUser.setRole(user.getRole());

        return userRepository.save(existingUser);
    }

    public void deleteUserById(UUID userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(existingUser);
    }

    public void deleteUserByUsername(String username) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(existingUser);
    }

    public void deleteUserByEmail(String email) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(existingUser);
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getUserByName(String fullName) {
        return userRepository.findByFullName(fullName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void updatePasswordByEmail(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void validateEmail(String email) {
        if (email == null || !Pattern.matches(EMAIL_REGEX, email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validateMobileNumber(String mobileNumber) {
        if (mobileNumber == null || !Pattern.matches(MOBILE_REGEX, mobileNumber)) {
            throw new IllegalArgumentException("Invalid mobile number format");
        }
    }

    private String generateUniqueUsername() {
        String prefix = "U";
        Random random = new Random();
        String username;
        do {
            int number = random.nextInt(10000);
            username = prefix + String.format("%04d", number); // Generates a username like U1234
        } while (userRepository.existsByUsername(username));
        return username;
    }
}
