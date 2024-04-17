package com.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderDetail implements Serializable{
    Long id;//主键
    String name;//名字
    String image;//图片
    Long orderId;//订单id
    Long dishId;//菜品id
    Long setmealId;//套餐id
    String dishFlavor;//口味
    Integer number;//数量
    BigDecimal amount;//金额
}
