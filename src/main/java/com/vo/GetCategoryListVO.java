package com.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class GetCategoryListVO implements Serializable {
    private Long id;
    private String type;
    private String name;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;
}
