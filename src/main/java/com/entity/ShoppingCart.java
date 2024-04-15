package com.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShoppingCart implements Serializable {

    private Long id;
    private Long userId;
    private Long dishId;
    private Long setmealId;
    private Integer number;
    private String dishFlavor;
    private String name;
    private String image;
    private BigDecimal amount;
    private LocalDateTime createTime;



}
