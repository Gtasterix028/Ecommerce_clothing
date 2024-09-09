package com.gtasterix.E_Commerce.controller;

import com.gtasterix.E_Commerce.Util.Response;
import com.gtasterix.E_Commerce.exception.ShoppingCartNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.model.ShoppingCart;
import com.gtasterix.E_Commerce.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shopping-carts")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping
    public ResponseEntity<Response> createShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        try {
            ShoppingCart createdShoppingCart = shoppingCartService.createShoppingCart(shoppingCart);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Shopping cart created successfully", createdShoppingCart, false));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getShoppingCartById(@PathVariable UUID id) {
        try {
            ShoppingCart shoppingCart = shoppingCartService.getShoppingCartById(id);
            return ResponseEntity.ok(new Response("Shopping cart retrieved successfully", shoppingCart, false));
        } catch (ShoppingCartNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateShoppingCart(@PathVariable UUID id, @RequestBody ShoppingCart shoppingCart) {
        try {
            ShoppingCart updatedShoppingCart = shoppingCartService.updateShoppingCart(id, shoppingCart);
            return ResponseEntity.ok(new Response("Shopping cart updated successfully", updatedShoppingCart, false));
        } catch (ShoppingCartNotFoundException | ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @GetMapping
    public ResponseEntity<Response> getAllShoppingCarts() {
        try {
            List<ShoppingCart> shoppingCarts = shoppingCartService.getAllShoppingCarts();
            return ResponseEntity.ok(new Response("Shopping carts retrieved successfully", shoppingCarts, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> patchShoppingCartById(@PathVariable UUID id, @RequestBody ShoppingCart shoppingCart) {
        try {
            ShoppingCart updatedShoppingCart = shoppingCartService.patchShoppingCartById(id, shoppingCart);
            return ResponseEntity.ok(new Response("Shopping cart updated successfully", updatedShoppingCart, false));
        } catch (ShoppingCartNotFoundException | ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteShoppingCartById(@PathVariable UUID id) {
        try {
            shoppingCartService.deleteShoppingCartById(id);
            return ResponseEntity.ok(new Response("Shopping cart deleted successfully", "Shopping cart erased", false));
        } catch (ShoppingCartNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }
}
