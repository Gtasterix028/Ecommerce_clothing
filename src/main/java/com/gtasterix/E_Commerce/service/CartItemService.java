package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.exception.CartItemNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.model.CartItem;
import com.gtasterix.E_Commerce.model.Product;
import com.gtasterix.E_Commerce.model.ShoppingCart;
import com.gtasterix.E_Commerce.repository.CartItemRepository;
import com.gtasterix.E_Commerce.repository.ProductRepository;
import com.gtasterix.E_Commerce.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    public CartItem createCartItem(CartItem cartItem) {
        try {
            validateCartItem(cartItem);
            ShoppingCart shoppingCart = shoppingCartRepository.findById(cartItem.getCart().getCartID())
                    .orElseThrow(() -> new ValidationException("Shopping cart with ID " + cartItem.getCart().getCartID() + " not found"));
            cartItem.setCart(shoppingCart);

            Product product = productRepository.findById(cartItem.getProduct().getProductID())
                    .orElseThrow(() -> new ValidationException("Product with ID " + cartItem.getProduct().getProductID() + " not found"));
            cartItem.setProduct(product);

            return cartItemRepository.save(cartItem);
        } catch (ValidationException e) {
            throw new ValidationException("Failed to create cart item: " + e.getMessage());
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
