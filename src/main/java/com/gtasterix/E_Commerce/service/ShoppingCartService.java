package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.exception.ShoppingCartNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
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

    public ShoppingCart createShoppingCart(ShoppingCart shoppingCart) {
        try {
            validateShoppingCart(shoppingCart);
            User user = userRepository.findById(shoppingCart.getUser().getUserID())
                    .orElseThrow(() -> new ValidationException("User with ID " + shoppingCart.getUser().getUserID() + " not found"));
            shoppingCart.setUser(user);  // This should include all fields of User
            return shoppingCartRepository.save(shoppingCart);
        } catch (ValidationException e) {
            throw new ValidationException("Failed to create shopping cart: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while creating shopping cart: " + e.getMessage());
        }
    }

    public ShoppingCart getShoppingCartById(UUID id) {
        try {
            return shoppingCartRepository.findById(id)
                    .orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart with ID " + id + " not found"));
        } catch (ShoppingCartNotFoundException e) {
            throw new ShoppingCartNotFoundException("Failed to retrieve shopping cart: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while retrieving shopping cart: " + e.getMessage());
        }
    }

    public ShoppingCart updateShoppingCart(UUID id, ShoppingCart shoppingCart) {
        try {
            validateShoppingCart(shoppingCart);
            if (!shoppingCartRepository.existsById(id)) {
                throw new ShoppingCartNotFoundException("Shopping cart with ID " + id + " not found");
            }
            shoppingCart.setCartID(id);
            return shoppingCartRepository.save(shoppingCart);
        } catch (ShoppingCartNotFoundException | ValidationException e) {
            throw new RuntimeException("Failed to update shopping cart: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while updating shopping cart: " + e.getMessage());
        }
    }

    public List<ShoppingCart> getAllShoppingCarts() {
        try {
            return shoppingCartRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while retrieving all shopping carts: " + e.getMessage());
        }
    }

    public ShoppingCart patchShoppingCartById(UUID id, ShoppingCart shoppingCart) {
        try {
            ShoppingCart existingShoppingCart = shoppingCartRepository.findById(id)
                    .orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart with ID " + id + " not found"));

            if (shoppingCart.getUser() != null) existingShoppingCart.setUser(shoppingCart.getUser());

            return shoppingCartRepository.save(existingShoppingCart);
        } catch (ShoppingCartNotFoundException e) {
            throw new ShoppingCartNotFoundException("Failed to patch shopping cart: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while patching shopping cart: " + e.getMessage());
        }
    }

    public void deleteShoppingCartById(UUID id) {
        try {
            ShoppingCart existingShoppingCart = shoppingCartRepository.findById(id)
                    .orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart with ID " + id + " not found"));

            shoppingCartRepository.delete(existingShoppingCart);
        } catch (ShoppingCartNotFoundException e) {
            throw new ShoppingCartNotFoundException("Failed to delete shopping cart: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while deleting shopping cart: " + e.getMessage());
        }
    }

    private void validateShoppingCart(ShoppingCart shoppingCart) {
        if (shoppingCart.getUser() == null) {
            throw new ValidationException("User cannot be null");
        }
    }
}
