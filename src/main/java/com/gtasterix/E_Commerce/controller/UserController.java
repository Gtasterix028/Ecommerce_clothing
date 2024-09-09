package com.gtasterix.E_Commerce.controller;

import com.gtasterix.E_Commerce.Util.Response;
import com.gtasterix.E_Commerce.model.User;
import com.gtasterix.E_Commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Response> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            Response response = new Response("User created successfully", createdUser, false);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Response response = new Response("Error creating user", null, true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable UUID id) {
        try {
            User user = userService.getUserById(id);
            Response response = new Response("User found", user, false);
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
            Response response = new Response("User found", user, false);
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
            Response response = new Response("User found", user, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("User not found", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateUserById(@PathVariable UUID id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUserById(id, user);
            Response response = new Response("User updated successfully", updatedUser, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("Error updating user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/username/{username}")
    public ResponseEntity<Response> updateUserByUsername(@PathVariable String username, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUserByUsername(username, user);
            Response response = new Response("User updated successfully", updatedUser, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("Error updating user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/email/{email}")
    public ResponseEntity<Response> updateUserByEmail(@PathVariable String email, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUserByEmail(email, user);
            Response response = new Response("User updated successfully", updatedUser, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("Error updating user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> patchUserById(@PathVariable UUID id, @RequestBody User user) {
        try {
            User patchedUser = userService.patchUserById(id, user);
            Response response = new Response("User patched successfully", patchedUser, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("Error patching user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/username/{username}")
    public ResponseEntity<Response> patchUserByUsername(@PathVariable String username, @RequestBody User user) {
        try {
            User patchedUser = userService.patchUserByUsername(username, user);
            Response response = new Response("User patched successfully", patchedUser, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("Error patching user", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/email/{email}")
    public ResponseEntity<Response> patchUserByEmail(@PathVariable String email, @RequestBody User user) {
        try {
            User patchedUser = userService.patchUserByEmail(email, user);
            Response response = new Response("User patched successfully", patchedUser, false);
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
    public ResponseEntity<Response> updatePasswordByEmail(@RequestParam("email") String email, @RequestParam("newPassword") String newPassword) {
        try {
            userService.updatePasswordByEmail(email, newPassword);
            Response response = new Response("Password updated successfully", null, false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response response = new Response("Error updating password", e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
