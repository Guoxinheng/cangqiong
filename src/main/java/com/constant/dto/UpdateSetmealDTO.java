package com.constant.dto;

import com.entity.SetmealDish;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateSetmealDTO implements Serializable {

    private  Long categoryId;//分类id
    private  String description;//套餐描述
    private  Long id;//套餐id
    private String image;//套餐图片
    private String name;//套餐名称
    private  BigDecimal price;//套餐价格
    private Integer status;//套餐状态
    private List<SetmealDish>  setmealDishes;//套餐关联的菜品
}
