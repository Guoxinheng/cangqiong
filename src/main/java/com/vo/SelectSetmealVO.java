package com.vo;

import com.entity.SetmealDish;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SelectSetmealVO {
    private Long categoryId;//分类id
    private String categoryName;//分类名称
    private String description;//套餐描述
    private Long id;//套餐id
    private String image;//套餐图片
    private String name;//套餐名字
    private BigDecimal price;//套餐价格
    private List<SetmealDish> setmealDishes;//套餐包含菜品
    private Integer status;//套餐状态
    private LocalDateTime updateTime;//套餐更新时间


}
