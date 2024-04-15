package com.vo;


import lombok.Data;

import java.util.List;

@Data
public class PageDishVO {
    Long total;
    List<DishVO> records;

}
