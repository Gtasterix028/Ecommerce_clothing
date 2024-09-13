package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.dto.CartItemDTO;
import com.gtasterix.E_Commerce.exception.CartItemNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.mapper.CartItemMapper;
import com.gtasterix.E_Commerce.model.CartItem;
import com.gtasterix.E_Commerce.model.Product;
import com.gtasterix.E_Commerce.model.ShoppingCart;
import com.gtasterix.E_Commerce.repository.CartItemRepository;
import com.gtasterix.E_Commerce.repository.ProductRepository;
import com.gtasterix.E_Commerce.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    public CartItemDTO createCartItem(CartItemDTO cartItemDTO) {
        ShoppingCart cart = shoppingCartRepository.findById(cartItemDTO.getCartID())
                .orElseThrow(() -> new ValidationException("Shopping Cart with ID " + cartItemDTO.getCartID() + " does not exist"));
        Product product = productRepository.findById(cartItemDTO.getProductID())
                .orElseThrow(() -> new ValidationException("Product with ID " + cartItemDTO.getProductID() + " does not exist"));

        CartItem cartItem = CartItemMapper.toEntity(cartItemDTO, cart, product);
        validateCartItem(cartItem);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return CartItemMapper.toDTO(savedCartItem);
    }

    public CartItemDTO getCartItemById(UUID id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item with ID " + id + " not found"));
        return CartItemMapper.toDTO(cartItem);
    }

    public List<CartItemDTO> getAllCartItems() {
        return cartItemRepository.findAll().stream()
                .map(CartItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CartItemDTO updateCartItem(UUID id, CartItemDTO cartItemDTO) {
        if (!cartItemRepository.existsById(id)) {
            throw new CartItemNotFoundException("Cart item with ID " + id + " not found");
        }

        ShoppingCart cart = shoppingCartRepository.findById(cartItemDTO.getCartID())
                .orElseThrow(() -> new ValidationException("Shopping Cart with ID " + cartItemDTO.getCartID() + " does not exist"));
        Product product = productRepository.findById(cartItemDTO.getProductID())
                .orElseThrow(() -> new ValidationException("Product with ID " + cartItemDTO.getProductID() + " does not exist"));

        CartItem cartItem = CartItemMapper.toEntity(cartItemDTO, cart, product);
        cartItem.setCartItemID(id);
        validateCartItem(cartItem);
        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        return CartItemMapper.toDTO(updatedCartItem);
    }

    public CartItemDTO patchCartItemById(UUID id, CartItemDTO cartItemDTO) {
        CartItem existingCartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item with ID " + id + " not found"));

        if (cartItemDTO.getCartID() != null) {
            ShoppingCart cart = shoppingCartRepository.findById(cartItemDTO.getCartID())
                    .orElseThrow(() -> new ValidationException("Shopping Cart with ID " + cartItemDTO.getCartID() + " does not exist"));
            existingCartItem.setCart(cart);
        }
        if (cartItemDTO.getProductID() != null) {
            Product product = productRepository.findById(cartItemDTO.getProductID())
                    .orElseThrow(() -> new ValidationException("Product with ID " + cartItemDTO.getProductID() + " does not exist"));
            existingCartItem.setProduct(product);
        }
        if (cartItemDTO.getQuantity() != null) {
            if (cartItemDTO.getQuantity() <= 0) {
                throw new ValidationException("Quantity must be greater than 0");
            }
            existingCartItem.setQuantity(cartItemDTO.getQuantity());
        }

        CartItem updatedCartItem = cartItemRepository.save(existingCartItem);
        return CartItemMapper.toDTO(updatedCartItem);
    }

    public void deleteCartItemById(UUID id) {
        CartItem existingCartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item with ID " + id + " not found"));

        cartItemRepository.delete(existingCartItem);
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
