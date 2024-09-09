package com.gtasterix.E_Commerce.controller;

import com.gtasterix.E_Commerce.Util.Response;
import com.gtasterix.E_Commerce.exception.CartItemNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.model.CartItem;
import com.gtasterix.E_Commerce.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<Response> createCartItem(@RequestBody CartItem cartItem) {
        try {
            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Cart item created successfully", createdCartItem, false));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "Validation error", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getCartItemById(@PathVariable UUID id) {
        try {
            CartItem cartItem = cartItemService.getCartItemById(id);
            return ResponseEntity.ok(new Response("Cart item retrieved successfully", cartItem, false));
        } catch (CartItemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage(), "Cart item not found", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateCartItem(@PathVariable UUID id, @RequestBody CartItem cartItem) {
        try {
            CartItem updatedCartItem = cartItemService.updateCartItem(id, cartItem);
            return ResponseEntity.ok(new Response("Cart item updated successfully", updatedCartItem, false));
        } catch (CartItemNotFoundException | ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "Validation or not found error", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> patchCartItemById(@PathVariable UUID id, @RequestBody CartItem cartItem) {
        try {
            CartItem updatedCartItem = cartItemService.patchCartItemById(id, cartItem);
            return ResponseEntity.ok(new Response("Cart item updated successfully", updatedCartItem, false));
        } catch (CartItemNotFoundException | ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "Validation or not found error", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteCartItemById(@PathVariable UUID id) {
        try {
            cartItemService.deleteCartItemById(id);
            return ResponseEntity.ok(new Response("Cart item deleted successfully", "Cart item erased", false));
        } catch (CartItemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage(), "Cart item not found", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @GetMapping
    public ResponseEntity<Response> getAllCartItems() {
        try {
            List<CartItem> cartItems = cartItemService.getAllCartItems();
            return ResponseEntity.ok(new Response("Cart items retrieved successfully", cartItems, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }
}
