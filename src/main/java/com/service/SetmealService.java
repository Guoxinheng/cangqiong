package com.service;

import com.dto.AddSetmealDTO;
import com.dto.SetmealListDTO;
import com.dto.UpdateSetmealDTO;
import com.entity.Setmeal;
import com.vo.*;

import java.util.List;

public interface SetmealService {
    List<SetmealListVO> getMealList(Long categoryId);


    List<SetmealDishVO> getSetmealDishById(Long id);

    List<Setmeal> list(Setmeal setmeal);

    List<DishItemVO> getDishItemById(Long id);

    PageSetmealVO page(SetmealListDTO setmealListDTO);
   //根据id查询套餐,套餐回显
    SelectSetmealVO getSetmealById(Long id);

    void updateStatus(String status, Long id);

    void saveSetmeal(AddSetmealDTO addSetmealDTO);

    void deleteSetmealWithSetmealDish(Long[] ids);

    void updateSetmeal(UpdateSetmealDTO updateSetmealDTO);
}
