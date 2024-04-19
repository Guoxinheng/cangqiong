package com.constant.dto;

import com.entity.DishFlavor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateDishAndDishFlavor {

    private Long categoryId;//分类id
    private String description;//描述
    private Long id;//当前id
    private String image;//图像地址
    private String name;//名字
    private BigDecimal price;//价格
    private Integer status;//状态
    private List<DishFlavor> flavors;//口味
}
