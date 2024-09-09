package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.exception.CartItemNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.model.CartItem;
import com.gtasterix.E_Commerce.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartItem createCartItem(CartItem cartItem) {
        try {
            validateCartItem(cartItem);
            return cartItemRepository.save(cartItem);
        } catch (ValidationException e) {
            throw new ValidationException("Validation error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while creating cart item: " + e.getMessage());
        }
    }

    public CartItem getCartItemById(UUID id) {
        try {
            return cartItemRepository.findById(id)
                    .orElseThrow(() -> new CartItemNotFoundException("Cart item with ID " + id + " not found"));
        } catch (CartItemNotFoundException e) {
            throw new CartItemNotFoundException("Cart item with ID " + id + " not found");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while retrieving cart item: " + e.getMessage());
        }
    }

    public CartItem updateCartItem(UUID id, CartItem cartItem) {
        try {
            validateCartItem(cartItem);
            if (!cartItemRepository.existsById(id)) {
                throw new CartItemNotFoundException("Cart item with ID " + id + " not found");
            }
            cartItem.setCartItemID(id);
            return cartItemRepository.save(cartItem);
        } catch (CartItemNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while updating cart item: " + e.getMessage());
        }
    }

    public List<CartItem> getAllCartItems() {
        try {
            return cartItemRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while retrieving all cart items: " + e.getMessage());
        }
    }

    public CartItem patchCartItemById(UUID id, CartItem cartItem) {
        try {
            CartItem existingCartItem = cartItemRepository.findById(id)
                    .orElseThrow(() -> new CartItemNotFoundException("Cart item with ID " + id + " not found"));

            if (cartItem.getCart() != null) existingCartItem.setCart(cartItem.getCart());
            if (cartItem.getProduct() != null) existingCartItem.setProduct(cartItem.getProduct());
            if (cartItem.getQuantity() != null) {
                if (cartItem.getQuantity() <= 0) {
                    throw new ValidationException("Quantity must be greater than 0");
                }
                existingCartItem.setQuantity(cartItem.getQuantity());
            }

            return cartItemRepository.save(existingCartItem);
        } catch (CartItemNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while partially updating cart item: " + e.getMessage());
        }
    }

    public void deleteCartItemById(UUID id) {
        try {
            CartItem existingCartItem = cartItemRepository.findById(id)
                    .orElseThrow(() -> new CartItemNotFoundException("Cart item with ID " + id + " not found"));

            cartItemRepository.delete(existingCartItem);
        } catch (CartItemNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while deleting cart item: " + e.getMessage());
        }
    }

    private void validateCartItem(CartItem cartItem) {
        if (cartItem.getCart() == null) {
            throw new ValidationException("Cart cannot be null");
        }
        if (cartItem.getProduct() == null) {
            throw new ValidationException("Product cannot be null");
        }
        if (cartItem.getQuantity() == null || cartItem.getQuantity() <= 0) {
            throw new ValidationException("Quantity must be greater than 0");
        }
    }
}
