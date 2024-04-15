package com.service;


import com.dto.ShoppingCartDTO;
import com.entity.ShoppingCart;

import java.util.List;


public interface ShoppingCartService {


    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> getShoppingList();

    void cleanShoppingCart();

    void deleteShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
