package com.service.impl;


import com.constant.dto.EditCategoryDTO;
import com.constant.dto.PageCategoryDTO;
import com.entity.Category;
import com.entity.Dish;
import com.entity.Setmeal;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mapper.CategoryMapper;
import com.mapper.DishMapper;
import com.mapper.SetmealMapper;
import com.service.CategoryService;
import com.vo.PageCategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private     CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
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

    /**
     * 获取分类页面信息
     * @param pageCategoryDTO 分页查询条件对象，包含页码和每页大小
     * @return 分类页面信息VO，包含总记录数和当前页的记录列表
     */
    @Override
    public PageCategoryVO getCategoryPage(PageCategoryDTO pageCategoryDTO) {
        // 初始化分页
        PageHelper.startPage(pageCategoryDTO.getPage(),pageCategoryDTO.getPageSize());
        // 执行分类分页查询
        Page<Category> list=categoryMapper.getCategoryPage(pageCategoryDTO);
        // 封装查询结果到VO对象
        PageCategoryVO pageCategoryVO=new PageCategoryVO();
        pageCategoryVO.setTotal(list.getTotal()); // 设置总记录数
        pageCategoryVO.setRecords(list.getResult()); // 设置当前页记录列表
        return pageCategoryVO;
    }

    /**
     * 更新分类的状态，同时根据分类类型，禁用该分类下的所有菜品或套餐。
     *
     * @param status 要更新的状态值，通常用于标记启用或禁用状态。
     * @param id 分类的唯一标识符。
     */
    @Override
    @Transactional
    public void updateStatus(Integer status, Long id) {
        // 修改当前分类状态
        categoryMapper.updateStatus(status,id);

        // 获取分类类型，以确定是菜品分类还是套餐分类
        Integer type= categoryMapper.getCategoryTypeById(id);

        // 根据分类类型，禁用该分类下的菜品或套餐
        if (type==1) // 如果是菜品分类，则禁用所有关联菜品
        {
           // 根据分类ID查找需要禁用的菜品
            List<Dish> dishes =dishMapper.getDishbyCategoryId(id);
            for (Dish dish : dishes) {
                // 更新每个菜品的状态
                dishMapper.updateStatus(status,dish.getId());
            }
        }
        if (type==2) // 如果是套餐分类，则禁用所有关联套餐
        {
          // 根据分类ID查找需要禁用的套餐
          List<Setmeal> setmealList= setmealMapper.getSetmealByCategoryId(id);
            for (Setmeal setmeal : setmealList) {
                // 更新每个套餐的状态
                setmeal.setStatus(status);
                setmealMapper.updateSetmeal(setmeal);
            }
        }
    }

    /**
     * 添加一个分类到数据库中。
     * @param category 需要添加的分类对象。
     */
    @Override
    public void addCategory(Category category) {
        // 通过categoryMapper将category对象添加到数据库
        category.setStatus(1);
        categoryMapper.addCategory(category);
    }

             /**
     * 删除特定分类及其关联的菜品或套餐。
     *
     * @param id 分类的唯一标识符。
     *
     * 删除指定ID的分类，同时根据分类类型，删除该分类下关联的菜品或套餐。
     */
    @Override
    @Transactional
    public void deleteCategory(Long id) {
        // 查询分类类型以决定后续操作
        Integer type=categoryMapper.getCategoryTypeById(id);
        // 根据ID删除分类
        categoryMapper.deleteCategory(id);
        // 如果是菜品分类，则删除该分类下的所有菜品
        if (type==1) {
            // 根据分类ID查询关联的菜品
            List<Dish> dishList=dishMapper.getDishbyCategoryId(id);
            List<Long> dishId=new ArrayList<>();
            // 收集菜品ID以备删除
            for (Dish dish : dishList) {
                dishId.add(dish.getId());
            }
            if (dishId.size()>0){
                // 批量删除菜品
                dishMapper.delete(dishId);
            }
        }

        // 如果是套餐分类，则删除该分类下的所有套餐
        if (type==2) {
            // 根据分类ID查询关联的套餐
            List<Setmeal> setmealList=setmealMapper.getSetmealByCategoryId(id);
            List<Long>    setmealId=new ArrayList<>();
            // 收集套餐ID以备删除
            for (Setmeal setmeal : setmealList) {
                setmealId.add(setmeal.getId());
            }
            // 批量删除套餐
            if (setmealId.size()>0) {
                setmealMapper.deleteSetmeal(setmealId);
            }
        }
    }


}
