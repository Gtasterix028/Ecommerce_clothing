package com.gtasterix.E_Commerce.mapper;

import com.gtasterix.E_Commerce.dto.CartItemDTO;
import com.gtasterix.E_Commerce.model.CartItem;
import com.gtasterix.E_Commerce.model.Product;
import com.gtasterix.E_Commerce.model.ShoppingCart;

public class CartItemMapper {

    public static CartItemDTO toDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartItemID(cartItem.getCartItemID());
        dto.setCartID(cartItem.getCart().getCartID());
        dto.setProductID(cartItem.getProduct().getProductID());
        dto.setQuantity(cartItem.getQuantity());
        return dto;
    }

    public static CartItem toEntity(CartItemDTO dto, ShoppingCart cart, Product product) {
        CartItem cartItem = new CartItem();
        cartItem.setCartItemID(dto.getCartItemID());
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(dto.getQuantity());
        return cartItem;
    }
}
