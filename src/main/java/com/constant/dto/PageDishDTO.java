package com.constant.dto;

import lombok.Data;


@Data
public class PageDishDTO {
    private Long categoryId;
    private String name;
    private Integer status;
    private int page;
    private int pageSize;

}
