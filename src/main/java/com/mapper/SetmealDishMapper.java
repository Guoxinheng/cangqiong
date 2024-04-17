package com.mapper;

import com.annotation.AutoFill;
import com.entity.SetmealDish;
import com.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    List<Long> selectSetmealIdsByDishId(Long id);

    void saveSetmealDish(@Param("setmealDishes") List<SetmealDish> setmealDishes);
     @AutoFill(OperationType.UPDATE)
    void deleteSetmealDish(@Param("ids") List<Long> ids);

    void deleteSetmealDishBySetmealId(Long id);
}
