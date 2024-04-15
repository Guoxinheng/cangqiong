package com.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Setmeal implements Serializable {
    Long id;
    Long categoryId;
    String name;
    BigDecimal price;
    Integer status;
    String description;
    String image;
    LocalDateTime createTime;
    LocalDateTime updateTime;
    Long createUser;
    Long updateUser;
}
