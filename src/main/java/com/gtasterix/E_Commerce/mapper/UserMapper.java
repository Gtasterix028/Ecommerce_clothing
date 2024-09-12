package com.gtasterix.E_Commerce.mapper;

import com.gtasterix.E_Commerce.dto.UserDTO;
import com.gtasterix.E_Commerce.model.User;

public class UserMapper {

    // Convert User entity to UserDTO
    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserID(user.getUserID());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setAddress(user.getAddress());
        dto.setRole(user.getRole());
        return dto;
    }


    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setUserID(dto.getUserID());
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setMobileNumber(dto.getMobileNumber());
        user.setAddress(dto.getAddress());
        user.setRole(dto.getRole());
        return user;
    }
}
