package com.vo;

import com.entity.DishFlavor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DishListVO implements Serializable {
    private Long id;
    private String name;
    private BigDecimal price;
    private String image;
    private Long categoryId;
    private Integer status;
    private String description;
    private String categoryName;
    private LocalDateTime updateTime;
    private List<DishFlavor> flavors;
}
