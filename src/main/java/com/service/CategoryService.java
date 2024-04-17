package com.service;

import com.dto.EditCategoryDTO;
import com.dto.PageCategoryDTO;
import com.entity.Category;
import com.vo.PageCategoryVO;

import java.util.List;

public interface CategoryService {
    void editCategory(EditCategoryDTO editCategoryDTO);

    List<Category> list(Integer type);

    PageCategoryVO getCategoryPage(PageCategoryDTO pageCategoryDTO);

    void updateStatus(Integer status, Long id);

    void addCategory(Category category);

    void deleteCategory(Long id);
}
