package com.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class SetmealDishVO implements Serializable {
    private  Integer copies;
    private  String description;
    private  String image;
    private  String name;
}
