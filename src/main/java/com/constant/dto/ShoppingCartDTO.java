package com.constant.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    String dishFlavor;
    Long dishId;
    Long setmealId;
}
