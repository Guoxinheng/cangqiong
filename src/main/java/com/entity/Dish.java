package com.entity;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data

public class Dish {
    private Long id;
    private String name;
    private BigDecimal price;
    private String image;
    private Long categoryId;
    private Integer status;
    private String description;
    private Long createUser;
    private Long updateUser;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
