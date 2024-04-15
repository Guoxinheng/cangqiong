package com.mapper;

import com.annotation.AutoFill;
import com.dto.PageDishDTO;
import com.entity.Dish;
import com.enumeration.OperationType;
import com.github.pagehelper.Page;
import com.vo.DishListVO;
import com.vo.DishVO;
import com.vo.PageDishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    @AutoFill(value=OperationType.INSERT)
    void save(Dish dish);


    Page<DishVO> getpage(PageDishDTO pageDishDTO);

   

    void delete(@Param("ids") List<Long> ids);
    @Select("select * from dish where id=#{id}")
    Dish selcectById(Long id);

    @Update("update dish set status=#{status} where id=#{id}")
    void updateStatus(@Param("status") Integer status, @Param("id") Long id);
    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);


    List<Dish> getDishList(Dish dish);

    List<Dish> list(Dish dish);

    List<Dish> getDishbyCategoryId(Long categoryId);
}
