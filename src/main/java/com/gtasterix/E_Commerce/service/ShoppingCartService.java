package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.dto.ShoppingCartDTO;
import com.gtasterix.E_Commerce.exception.ShoppingCartNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.mapper.ShoppingCartMapper;
import com.gtasterix.E_Commerce.model.ShoppingCart;
import com.gtasterix.E_Commerce.model.User;
import com.gtasterix.E_Commerce.repository.ShoppingCartRepository;
import com.gtasterix.E_Commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    public ShoppingCartDTO createShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        validateShoppingCartDTO(shoppingCartDTO);
        User user = userRepository.findById(shoppingCartDTO.getUserID())
                .orElseThrow(() -> new ValidationException("User with ID " + shoppingCartDTO.getUserID() + " not found"));
        ShoppingCart shoppingCart = ShoppingCartMapper.toEntity(shoppingCartDTO, user);
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);
        return ShoppingCartMapper.toDTO(savedShoppingCart);
    }

    public ShoppingCartDTO getShoppingCartById(UUID id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart with ID " + id + " not found"));
        return ShoppingCartMapper.toDTO(shoppingCart);
    }

    public ShoppingCartDTO updateShoppingCart(UUID id, ShoppingCartDTO shoppingCartDTO) {
        validateShoppingCartDTO(shoppingCartDTO);
        if (!shoppingCartRepository.existsById(id)) {
            throw new ShoppingCartNotFoundException("Shopping cart with ID " + id + " not found");
        }
        User user = userRepository.findById(shoppingCartDTO.getUserID())
                .orElseThrow(() -> new ValidationException("User with ID " + shoppingCartDTO.getUserID() + " not found"));
        ShoppingCart shoppingCart = ShoppingCartMapper.toEntity(shoppingCartDTO, user);
        shoppingCart.setCartID(id);
        ShoppingCart updatedShoppingCart = shoppingCartRepository.save(shoppingCart);
        return ShoppingCartMapper.toDTO(updatedShoppingCart);
    }

    public List<ShoppingCartDTO> getAllShoppingCarts() {
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAll();
        return shoppingCarts.stream()
                .map(ShoppingCartMapper::toDTO)
                .toList();
    }

    public ShoppingCartDTO patchShoppingCartById(UUID id, ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart existingShoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart with ID " + id + " not found"));

        if (shoppingCartDTO.getUserID() != null) {
            User user = userRepository.findById(shoppingCartDTO.getUserID())
                    .orElseThrow(() -> new ValidationException("User with ID " + shoppingCartDTO.getUserID() + " not found"));
            existingShoppingCart.setUser(user);
        }

        ShoppingCart updatedShoppingCart = shoppingCartRepository.save(existingShoppingCart);
        return ShoppingCartMapper.toDTO(updatedShoppingCart);
    }

    public void deleteShoppingCartById(UUID id) {
        ShoppingCart existingShoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart with ID " + id + " not found"));
        shoppingCartRepository.delete(existingShoppingCart);
    }

    private void validateShoppingCartDTO(ShoppingCartDTO shoppingCartDTO) {
        if (shoppingCartDTO.getUserID() == null) {
            throw new ValidationException("User ID cannot be null");
        }
    }
}
