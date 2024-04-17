package com.mapper;

import com.annotation.AutoFill;
import com.dto.SetmealListDTO;
import com.entity.Setmeal;
import com.entity.SetmealDish;
import com.enumeration.OperationType;
import com.github.pagehelper.Page;
import com.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SetmealMapper {
    List<SetmealListVO> getMealList(Long categoryId);

    List<SetmealDishVO> getSetmealDishById(Long id);

    List<Setmeal> list(Setmeal setmeal);
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long id);

    Page<AdminSetmeaListVO> page(SetmealListDTO setmealListDTO);

    Setmeal getSetmealById(Long id);

    List<SetmealDish> getSetmealDishBySetmealId(Long id);

    @Update("update setmeal set status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    void updateStatus(@Param("status") String status,@Param("id") Long id,@Param("updateTime") LocalDateTime updateTime,@Param("updateUser") Long updateUser);
     @AutoFill(OperationType.INSERT)
    void saveSetmeal(Setmeal setmeal);

    void deleteSetmeal(@Param("ids") List<Long> ids);
     @AutoFill(OperationType.UPDATE)
    void updateSetmeal(Setmeal setmeal);
   @Select("select * from setmeal where category_id = #{categoryId}")
    List<Setmeal> getSetmealByCategoryId(Long id);
}
