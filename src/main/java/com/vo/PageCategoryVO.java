package com.vo;

import com.entity.Category;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageCategoryVO implements Serializable {
    Long total;
    List<Category> records;
}
