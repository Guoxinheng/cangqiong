package com.controller.admin;

import com.constant.dto.EditCategoryDTO;
import com.constant.dto.PageCategoryDTO;
import com.entity.Category;
import com.result.Result;
import com.service.CategoryService;
import com.vo.PageCategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("amdinCategoryController")
@RequestMapping("/admin/category")
@Api(tags = "菜品分类管理")
@Slf4j
public class CategoryController {
     @Autowired
     private CategoryService categoryService;

     @PutMapping()
    public Result<String> edit(@RequestBody EditCategoryDTO editCategoryDTO){



         categoryService.editCategory(editCategoryDTO);
         return Result.success("修改成功");
     }
     @GetMapping("/list")
    public Result<List<Category>> list(Integer type)
     {
       List<Category>  list= categoryService.list(type);
       return Result.success(list);
     }
    /**
     * 菜品分类分页查询
     *
     * @param pageCategoryDTO 分页查询参数对象，包含查询条件和分页信息
     * @return 返回菜品分类的分页查询结果，包含分类信息和分页状态
     */
    @GetMapping("/page")
    @ApiOperation("菜品分类分页查询")
    public Result<PageCategoryVO> getCategoryPage( PageCategoryDTO pageCategoryDTO)
    {
        log.info("分页查询菜品分类");
        // 调用服务层方法，进行菜品分类的分页查询
        PageCategoryVO list= categoryService.getCategoryPage(pageCategoryDTO);
        // 返回查询结果
        return Result.success(list);
    }
    /**
     * 修改菜品分类状态
     *
     * @param status 要修改的状态值，是一个整数
     * @param id 要修改的菜品分类的ID，是一个长整数
     * @return 返回一个结果对象，其中包含修改操作的成功信息
     */
    @PostMapping("/status/{status}")
    @ApiOperation("修改菜品分类状态")
    public Result<String> updateStatus(@PathVariable Integer status, Long id) {
        log.info("修改菜品分类状态");
        // 调用服务层方法，修改菜品分类状态
        categoryService.updateStatus(status, id);
        // 返回修改成功的提示信息
        return Result.success("修改成功");
    }
    /**
     * 新增分类
     *
     * @param category 分类对象，通过RequestBody接收前端传来的分类信息
     * @return 返回操作结果，包含新增成功的提示信息
     */
    @PostMapping()
    @ApiOperation("新增分类")
    public Result<String> addCategory(@RequestBody Category category) {
        log.info("新增分类");
        // 调用服务层方法，新增分类
        categoryService.addCategory(category);
        // 返回新增成功的提示信息
        return Result.success("新增成功");
    }
    /**
     * 删除分类
     *
     * @param id 分类的唯一标识符
     * @return 返回一个结果对象，包含删除操作的成功信息
     */
    @DeleteMapping()
    @ApiOperation("删除分类")
    public Result<String> deleteCategory(Long id) {
        log.info("删除分类{}",id);
        // 调用服务层方法，执行分类的删除操作
        categoryService.deleteCategory(id);
        // 返回删除成功的提示信息
        return Result.success("删除成功");
    }
}
