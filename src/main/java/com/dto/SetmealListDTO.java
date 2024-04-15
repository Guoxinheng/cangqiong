package com.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class SetmealListDTO implements Serializable {
    Long categoryId;
    String name;
    Integer status;
    int page;
    int pageSize;

}
