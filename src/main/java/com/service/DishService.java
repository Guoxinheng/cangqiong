package com.service;

import com.constant.dto.AddDishDTO;
import com.constant.dto.PageDishDTO;
import com.constant.dto.UpdateDishAndDishFlavor;
import com.entity.Dish;
import com.vo.*;

import java.util.List;

public interface DishService {
    void save(AddDishDTO addDishDTO);

    PageDishVO getpage(PageDishDTO pageDishDTO);

    List<DishVO> list(Long categoryId);

    void delete(List<Long> ids);

    void updateStatus(Integer status, Long id);

    void updateDishAndDishFlavor(UpdateDishAndDishFlavor updateDishAndDishFlavor);

    DishAndDishFlavorVO selcectById(Long id);


    List<DishVO> listWithFlavor(Dish dish);

    List<Dish> getDishbyCategoryId(Long categoryId);
}
