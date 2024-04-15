package com.mapper;

import com.annotation.AutoFill;
import com.entity.Category;
import com.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Update("update Category set  sort=#{sort},update_time=#{updateTime},name=#{name} ,type=#{type},update_user=#{updateUser},update_time=#{updateTime} where id=#{id}")
    @AutoFill(value= OperationType.UPDATE)
    void editCategory(Category editCategoryDTO);

    List<Category> list(Integer type);
     @Select("select name from category where id=#{categoryId}")
    String getCategoryname(Long categoryId);
}
