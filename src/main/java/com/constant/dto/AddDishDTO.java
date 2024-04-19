package com.constant.dto;


import com.entity.DishFlavor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class AddDishDTO implements Serializable {

    private Long categoryId;
    private String description;
    private Long id;
    private String image;
    private String name;
    private BigDecimal price;
    private Integer status;
    private List<DishFlavor> flavors;
}
