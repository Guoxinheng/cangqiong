package com.mapper;

import com.annotation.AutoFill;
import com.constant.dto.PageDishDTO;
import com.entity.Dish;
import com.enumeration.OperationType;
import com.github.pagehelper.Page;
import com.vo.DishVO;
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

    /**
     * 根据类别ID获取菜品列表
     * @param categoryId 菜品类别的ID
     * @return 返回对应类别ID的菜品列表
     */
    List<Dish> getDishbyCategoryId(Long categoryId);

    /**
     * 根据状态查询记录数
     *
     * @param i 状态值，用于筛选特定状态的记录
     * @return 返回满足条件的记录数量
     */
    Long selectCountByStatus(int i);
}
