package com.gtasterix.E_Commerce.controller;

import com.gtasterix.E_Commerce.Util.Response;
import com.gtasterix.E_Commerce.dto.UserDTO;
import com.gtasterix.E_Commerce.mapper.UserMapper;
import com.gtasterix.E_Commerce.model.User;
import com.gtasterix.E_Commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Response> createUser(@RequestBody UserDTO userDTO) {
        try {
            User user = UserMapper.toEntity(userDTO);
            User createdUser = userService.createUser(userDTO);
            UserDTO createdUserDTO = UserMapper.toDTO(createdUser);
            Response response = new Response("User created successfully", createdUserDTO, false);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Response response = new Response("Error creating user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Response> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            List<UserDTO> userDTOs = users.stream().map(UserMapper::toDTO).collect(Collectors.toList());
            Response response = new Response("Users retrieved successfully", userDTOs, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("Error retrieving users", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable UUID id) {
        try {
            User user = userService.getUserById(id);
            UserDTO userDTO = UserMapper.toDTO(user);
            Response response = new Response("User found", userDTO, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("User not found", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Response> getUserByUserName(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            UserDTO userDTO = UserMapper.toDTO(user);
            Response response = new Response("User found", userDTO, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("User not found", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/name/{fullName}")
    public ResponseEntity<Response> getUserByName(@PathVariable String fullName) {
        try {
            User user = userService.getUserByName(fullName);
            UserDTO userDTO = UserMapper.toDTO(user);
            Response response = new Response("User found", userDTO, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("User not found", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateUserById(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        try {
            User user = UserMapper.toEntity(userDTO);
            User updatedUser = userService.updateUserById(id, userDTO);
            UserDTO updatedUserDTO = UserMapper.toDTO(updatedUser);
            Response response = new Response("User updated successfully", updatedUserDTO, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("Error updating user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/username/{username}")
    public ResponseEntity<Response> updateUserByUsername(@PathVariable String username, @RequestBody UserDTO userDTO) {
        try {
            User user = UserMapper.toEntity(userDTO);
            User updatedUser = userService.updateUserByUsername(username, userDTO);
            UserDTO updatedUserDTO = UserMapper.toDTO(updatedUser);
            Response response = new Response("User updated successfully", updatedUserDTO, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("Error updating user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/email/{email}")
    public ResponseEntity<Response> updateUserByEmail(@PathVariable String email, @RequestBody UserDTO userDTO) {
        try {
            User user = UserMapper.toEntity(userDTO);
            User updatedUser = userService.updateUserByEmail(email, userDTO);
            UserDTO updatedUserDTO = UserMapper.toDTO(updatedUser);
            Response response = new Response("User updated successfully", updatedUserDTO, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("Error updating user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> patchUserById(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        try {
            User user = UserMapper.toEntity(userDTO);
            User patchedUser = userService.patchUserById(id, userDTO);
            UserDTO patchedUserDTO = UserMapper.toDTO(patchedUser);
            Response response = new Response("User patched successfully", patchedUserDTO, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("Error patching user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/username/{username}")
    public ResponseEntity<Response> patchUserByUsername(@PathVariable String username, @RequestBody UserDTO userDTO) {
        try {
            User user = UserMapper.toEntity(userDTO);
            User patchedUser = userService.patchUserByUsername(username, userDTO);
            UserDTO patchedUserDTO = UserMapper.toDTO(patchedUser);
            Response response = new Response("User patched successfully", patchedUserDTO, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("Error patching user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/email/{email}")
    public ResponseEntity<Response> patchUserByEmail(@PathVariable String email, @RequestBody UserDTO userDTO) {
        try {
            User user = UserMapper.toEntity(userDTO);
            User patchedUser = userService.patchUserByEmail(email, userDTO);
            UserDTO patchedUserDTO = UserMapper.toDTO(patchedUser);
            Response response = new Response("User patched successfully", patchedUserDTO, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("Error patching user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUserById(@PathVariable UUID id) {
        try {
            userService.deleteUserById(id);
            Response response = new Response("User deleted successfully", null, false);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            Response response = new Response("Error deleting user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/username/{username}")
    public ResponseEntity<Response> deleteUserByUsername(@PathVariable String username) {
        try {
            userService.deleteUserByUsername(username);
            Response response = new Response("User deleted successfully", null, false);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            Response response = new Response("Error deleting user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<Response> deleteUserByEmail(@PathVariable String email) {
        try {
            userService.deleteUserByEmail(email);
            Response response = new Response("User deleted successfully", null, false);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            Response response = new Response("Error deleting user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<Response> updatePasswordByEmail(
            @RequestParam("email") String email,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword) {

        try {
            userService.updatePasswordByEmail(email, oldPassword, newPassword);
            Response response = new Response("Password updated successfully", null, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Response response = new Response("Error updating password", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Response response = new Response("Error updating password", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
