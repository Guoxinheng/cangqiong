package com.controller.admin;

import com.constant.dto.AddDishDTO;
import com.constant.dto.PageDishDTO;
import com.constant.dto.UpdateDishAndDishFlavor;
import com.entity.Dish;
import com.result.Result;
import com.service.DishService;
import com.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Api("菜品接口相关操作")
public class DishController {
    @Autowired
   private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping
    @ApiOperation("新增菜品")
    public Result<String> save(@RequestBody AddDishDTO addDishDTO)
    {
        //当进行修改的时候删除对应的缓存
       cleanCache("dish_"+addDishDTO.getCategoryId());
        dishService.save(addDishDTO);
        return Result.success("新增成功");
    }

    @GetMapping("/page")
    @ApiOperation("菜品的分页查询")
    public Result<PageDishVO> getpage(PageDishDTO pageDishDTO)
    {
        PageDishVO pageDishVO=  dishService.getpage(pageDishDTO);
        return Result.success(pageDishVO);
    }

    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result<String> delete(@RequestParam List<Long> ids)
    {
        cleanCache("dish_*");
        dishService.delete(ids);
        //清除所有菜品缓存

        return Result.success("删除成功");
    }
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long  categoryId)
    {

         List<Dish> list=  dishService.getDishbyCategoryId(categoryId);

      return   Result.success(list);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public  Result<String> updateStatus(@PathVariable("status") Integer status,Long id)
    {
        cleanCache("dish_*");
        dishService.updateStatus(status,id);
        return Result.success("修改成功");
    }
    @PutMapping()
    @ApiOperation("修改菜品")
    public Result<String> updateDish(@RequestBody UpdateDishAndDishFlavor updateDishAndDishFlavor)
    {
        //清除缓存数据
       cleanCache("dish_*");
        dishService.updateDishAndDishFlavor(updateDishAndDishFlavor);
        return Result.success("修改成功");
    }
   @GetMapping("/{id}")
    @ApiOperation("数据回显")
    public Result<DishAndDishFlavorVO> getById(@PathVariable("id") Long id)
   {
       DishAndDishFlavorVO dishAndDishFlavorVO= dishService.selcectById(id);
       return Result.success(dishAndDishFlavorVO);
   }

   private void cleanCache(String pattern)
   {
       Set keys = redisTemplate.keys(pattern);
       redisTemplate.delete(keys);
   }
 }
