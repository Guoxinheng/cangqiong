package com.constant.dto;

import lombok.Data;

@Data
public class EditCategoryDTO {
    private Long id;
    private String type;
    private String name;
    private Integer sort;
}
