package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.dto.UserDTO;
import com.gtasterix.E_Commerce.exception.EmailAlreadyExistsException;
import com.gtasterix.E_Commerce.exception.ResourceNotFoundException;
import com.gtasterix.E_Commerce.exception.UserNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.model.Role;
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

    public User createUser(UserDTO userDTO) {
        validateUserDTO(userDTO);

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = convertDTOToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUsername(generateUniqueUsername());

        return userRepository.save(user);
    }

    public User updateUserById(UUID userId, UserDTO userDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validateUserDTO(userDTO);

        updateExistingUserFromDTO(existingUser, userDTO);

        return userRepository.save(existingUser);
    }

    public User updateUserByUsername(String username, UserDTO userDTO) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validateUserDTO(userDTO);

        updateExistingUserFromDTO(existingUser, userDTO);

        return userRepository.save(existingUser);
    }

    public User updateUserByEmail(String email, UserDTO userDTO) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validateUserDTO(userDTO);

        updateExistingUserFromDTO(existingUser, userDTO);

        return userRepository.save(existingUser);
    }

    public User patchUserById(UUID userId, UserDTO userDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        patchExistingUserFromDTO(existingUser, userDTO);

        return userRepository.save(existingUser);
    }

    public User patchUserByUsername(String username, UserDTO userDTO) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        patchExistingUserFromDTO(existingUser, userDTO);

        return userRepository.save(existingUser);
    }

    public User patchUserByEmail(String email, UserDTO userDTO) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        patchExistingUserFromDTO(existingUser, userDTO);

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

    public void updatePasswordByEmail(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        // Check if the old password matches
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // Update the password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    private void validateUserDTO(UserDTO userDTO) {

        if (userDTO.getFullName() == null || userDTO.getFullName().isEmpty()) {
            throw new ValidationException("Full name is required");
        }
        if (userDTO.getEmail() == null || !Pattern.matches(EMAIL_REGEX, userDTO.getEmail())) {
            throw new ValidationException("Email should be valid");
        }
        if (userDTO.getMobileNumber() == null || !Pattern.matches(MOBILE_REGEX, userDTO.getMobileNumber())) {
            throw new ValidationException("Mobile number must be valid and start with 6-9");
        }
        if (userDTO.getAddress() == null || userDTO.getAddress().length() < 5 || userDTO.getAddress().length() > 255) {
            throw new ValidationException("Address must be between 5 and 255 characters");
        }
        if (userDTO.getRole() == null) {
            throw new ValidationException("Role is required");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().length() < 6) {
            throw new ValidationException("Password must be at least 6 characters long");
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

    private User convertDTOToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUserID(userDTO.getUserID());
        user.setUsername(userDTO.getUsername());
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setMobileNumber(userDTO.getMobileNumber());
        user.setAddress(userDTO.getAddress());
        user.setRole(userDTO.getRole());
        return user;
    }

    private void updateExistingUserFromDTO(User existingUser, UserDTO userDTO) {
        existingUser.setFullName(userDTO.getFullName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setMobileNumber(userDTO.getMobileNumber());
        existingUser.setAddress(userDTO.getAddress());
        existingUser.setRole(userDTO.getRole());
    }

    private void patchExistingUserFromDTO(User existingUser, UserDTO userDTO) {
        if (userDTO.getFullName() != null) existingUser.setFullName(userDTO.getFullName());
        if (userDTO.getEmail() != null) {
            validateEmail(userDTO.getEmail());
            existingUser.setEmail(userDTO.getEmail());
        }
        if (userDTO.getMobileNumber() != null) {
            validateMobileNumber(userDTO.getMobileNumber());
            existingUser.setMobileNumber(userDTO.getMobileNumber());
        }
        if (userDTO.getAddress() != null) existingUser.setAddress(userDTO.getAddress());
        if (userDTO.getRole() != null) existingUser.setRole(userDTO.getRole());
    }

    private void validateEmail(String email) {
        if (email == null || !Pattern.matches(EMAIL_REGEX, email)) {
            throw new ValidationException("Invalid email format");
        }
    }

    private void validateMobileNumber(String mobileNumber) {
        if (mobileNumber == null || !Pattern.matches(MOBILE_REGEX, mobileNumber)) {
            throw new ValidationException("Invalid mobile number format");
        }
    }
}
