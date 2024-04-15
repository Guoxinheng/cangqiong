package com.dto;

import com.entity.SetmealDish;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class AddSetmealDTO implements Serializable {
    Long categoryId;//分类id
    String description;//套餐描述
    Long id;//套餐id
    String image;//套餐图片
    String name;//套餐名字
    BigDecimal price;//套餐价格
    List<SetmealDish> setmealDishes;//套餐所包含的菜品
    Integer status;//套餐状态
}
