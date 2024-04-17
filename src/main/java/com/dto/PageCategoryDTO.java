package com.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageCategoryDTO implements Serializable {
    private int page;//当前页
    private int pageSize;//每一页的页码
    private String name;//分类名称
    private Integer type;//分类类型
}
