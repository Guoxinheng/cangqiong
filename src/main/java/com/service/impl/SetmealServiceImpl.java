package com.service.impl;


import com.context.BaseContext;
import com.dto.AddSetmealDTO;
import com.dto.SetmealListDTO;
import com.dto.UpdateSetmealDTO;
import com.entity.Setmeal;
import com.entity.SetmealDish;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mapper.CategoryMapper;
import com.mapper.SetmealDishMapper;
import com.mapper.SetmealMapper;
import com.service.SetmealService;
import com.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
     private SetmealMapper setmealMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Override
    public List<SetmealListVO> getMealList(Long categoryId) {

         //根据分类id查询套餐
      List<SetmealListVO> list=setmealMapper.getMealList(categoryId);
        return list;
    }



    @Override
    public List<SetmealDishVO> getSetmealDishById(Long id) {

        List<SetmealDishVO> list=   setmealMapper.getSetmealDishById(id);

        return list;
    }

    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

    @Override
    //电脑端的分页查询套餐
    @Transactional
    public PageSetmealVO page(SetmealListDTO setmealListDTO) {
        PageSetmealVO pageSetmealVO =new PageSetmealVO();
        //开启分页查询
        PageHelper.startPage(setmealListDTO.getPage(),setmealListDTO.getPageSize());
        Page<AdminSetmeaListVO> page = setmealMapper.page(setmealListDTO);
        List<AdminSetmeaListVO> result = page.getResult();
        result.forEach(item->{
            item.setCategoryName(categoryMapper.getCategoryname(item.getCategoryId()));
        });
        pageSetmealVO.setRecords(result);
        pageSetmealVO.setTotal(page.getTotal());

        return pageSetmealVO;
    }

    @Override
    @Transactional
    //根据id查询套餐,套餐的回显
    public SelectSetmealVO getSetmealById(Long id) {
        //先根据id查询套餐信息
        Setmeal  setmeal=new Setmeal();
         setmeal=setmealMapper.getSetmealById(id);
        //然后根据套餐id查询套餐包含的菜品信息
         List<SetmealDish> list=setmealMapper.getSetmealDishBySetmealId(id);
         //然后把套餐信息和菜品信息进行封装
        //再查询套餐对应分类名称
        SelectSetmealVO selectSetmealVO = new SelectSetmealVO();
        BeanUtils.copyProperties(setmeal,selectSetmealVO);
        selectSetmealVO.setSetmealDishes(list);
        selectSetmealVO.setCategoryName(categoryMapper.getCategoryname(setmeal.getCategoryId()));
        return selectSetmealVO;
    }

    @Override
    public void updateStatus(String status, Long id) {
        LocalDateTime updateTime=LocalDateTime.now();
        Long updateUser= BaseContext.getCurrentId();
        setmealMapper.updateStatus(status,id,updateTime,updateUser);
    }

    @Override
    @Transactional
    public void saveSetmeal(AddSetmealDTO addSetmealDTO) {
        //1.保存套餐的基本信息到setmeal表中，获取id
       Setmeal setmeal=new Setmeal();
       BeanUtils.copyProperties(addSetmealDTO,setmeal);
       setmealMapper.saveSetmeal(setmeal);
        //2.保存套餐和菜品的关联信息到setmeal_dish表中
        List<SetmealDish> setmealDishes=addSetmealDTO.getSetmealDishes();
        setmealDishes.forEach(item->{
            item.setSetmealId(setmeal.getId());

        });
        setmealDishMapper.saveSetmealDish(setmealDishes);

    }

    @Override
    @Transactional
    public void deleteSetmealWithSetmealDish(Long[] ids) {
        //先删除菜品和套餐关联表的数据
          setmealDishMapper.deleteSetmealDish(ids);
        //再删除套餐表中的数据
         setmealMapper.deleteSetmeal(ids);

    }

    @Override
    @Transactional
    public void updateSetmeal(UpdateSetmealDTO updateSetmealDTO) {
        //修改套餐的数据
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(updateSetmealDTO,setmeal);
        setmealMapper.updateSetmeal(setmeal);
        //先删除套餐和菜品的关联数据
        setmealDishMapper.deleteSetmealDishBySetmealId(setmeal.getId());
        List<SetmealDish> setmealDishes=updateSetmealDTO.getSetmealDishes();
        setmealDishes.forEach(item->{
            item.setSetmealId(setmeal.getId());
        });
        //然后添加套餐和菜品的关联数据
        setmealDishMapper.saveSetmealDish(setmealDishes);
    }
}
