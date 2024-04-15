package com.mapper;

import com.annotation.AutoFill;
import com.entity.DishFlavor;
import com.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {


    void save(@Param("list") List<DishFlavor> dishFlavor);

    void delete(@Param("dishId") Long id);

    void updateDishFlavor(DishFlavor dishFlavor);
    @Select("select * from dish_flavor where dish_id=#{id}")
    List<DishFlavor> selectById(@Param("id") Long id);
    //@Insert("insert into dish_flavor(name,value,dish_id) values(#{name},#{value},#{dishId})")
    void saveFlavor(DishFlavor flavor);
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
