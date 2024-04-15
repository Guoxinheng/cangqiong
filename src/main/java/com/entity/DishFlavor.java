package com.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class DishFlavor implements Serializable {
    private static final long serialVersionUID = 1L;
    private   Long id;
    private   String name;
    private  String value;
    private Long dishId;

}
