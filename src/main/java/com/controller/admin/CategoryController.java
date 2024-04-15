package com.controller.admin;

import com.dto.EditCategoryDTO;
import com.dto.PageDishDTO;
import com.entity.Category;
import com.result.Result;
import com.service.CategoryService;
import com.vo.PageDishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("amdinCategoryController")
@RequestMapping("/admin/category")
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

}
