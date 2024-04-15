package com.service.impl;


import com.dto.EditCategoryDTO;
import com.entity.Category;
import com.mapper.CategoryMapper;
import com.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private     CategoryMapper categoryMapper;
    @Override
    public void editCategory(EditCategoryDTO editCategoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(editCategoryDTO,category);
        categoryMapper.editCategory(category);

    }

    @Override
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }
}
