package com.mapper;

import com.annotation.AutoFill;
import com.constant.dto.PageCategoryDTO;
import com.entity.Category;
import com.enumeration.OperationType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Update("update Category set  sort=#{sort},update_time=#{updateTime},name=#{name} ,type=#{type},update_user=#{updateUser},update_time=#{updateTime} where id=#{id}")
    @AutoFill(value= OperationType.UPDATE)
    void editCategory(Category editCategoryDTO);

    List<Category> list(Integer type);
     @Select("select name from category where id=#{categoryId}")
    String getCategoryname(Long categoryId);

    Page<Category> getCategoryPage(PageCategoryDTO pageCategoryDTO);
    /**
     * 更新分类的状态。
     *
     * @param status 新的状态值，待更新到数据库中。
     * @param id 分类的唯一标识符，用于指定需要更新的分类。
     * @return void 方法没有返回值。
     */
    @Update("update category set status=#{status} where id=#{id}")
    void updateStatus(@Param("status") Integer status,@Param("id") Long id);
    /**
     * 通过ID获取类别类型。
     *
     * @param id 类别的ID，类型为Long。
     * @return 返回类别对应的类型，类型为Integer。
     */
    @Select("select type from category where id=#{id}")
    Integer getCategoryTypeById(Long id);
    /**
     * 添加一个分类信息。
     * 通过@AutoFill注解指定，在插入操作时自动填充特定信息。
     *
     * @param category 分类对象，包含分类的详细信息。
     *                 该对象将被插入到数据库中，用于新增一个分类。
     * @return 无返回值。
     */
    @AutoFill(OperationType.INSERT)
    void addCategory(Category category);
    @Delete("delete from category where id=#{id}")
    void deleteCategory(Long id);
}
