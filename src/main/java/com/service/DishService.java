package com.service;

import com.dto.AddDishDTO;
import com.dto.PageDishDTO;
import com.dto.UpdateDishAndDishFlavor;
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
