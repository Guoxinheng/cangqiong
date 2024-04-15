package com.service;

import com.dto.EditCategoryDTO;
import com.entity.Category;

import java.util.List;

public interface CategoryService {
    void editCategory(EditCategoryDTO editCategoryDTO);

    List<Category> list(Integer type);
}
