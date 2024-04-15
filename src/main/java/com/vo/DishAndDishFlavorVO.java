package com.vo;

import com.entity.DishFlavor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DishAndDishFlavorVO {
    private Long categoryId;
    private String description;
    private Long id;
    private String image;
    private String name;
    private BigDecimal price;
    private Integer status;
    private List<DishFlavor> flavors;
}
