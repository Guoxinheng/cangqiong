package com.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminSetmeaListVO implements Serializable {
    private  Long id;
    private Long categoryId;
    private  String name;
    private BigDecimal price;
    private Integer status;
    private String description;
    private String image;
    private LocalDateTime updateTime;
    private String categoryName;

}
