package com.gtasterix.E_Commerce.mapper;

import com.gtasterix.E_Commerce.dto.ShoppingCartDTO;
import com.gtasterix.E_Commerce.model.ShoppingCart;
import com.gtasterix.E_Commerce.model.User;

public class ShoppingCartMapper {

    public static ShoppingCartDTO toDTO(ShoppingCart shoppingCart) {
        ShoppingCartDTO dto = new ShoppingCartDTO();
        dto.setCartID(shoppingCart.getCartID());
        dto.setUserID(shoppingCart.getUser().getUserID());
        return dto;
    }

    public static ShoppingCart toEntity(ShoppingCartDTO dto, User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartID(dto.getCartID());
        shoppingCart.setUser(user);
        return shoppingCart;
    }
}
