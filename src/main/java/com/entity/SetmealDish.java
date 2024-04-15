package com.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SetmealDish implements Serializable {
    private Long id;//关系id
    private Long setmealId;//套餐id
    private Long dishId;//菜品id
    private String name;//关系名字
    private Integer copies;//份数
    private BigDecimal price;//价格

}
