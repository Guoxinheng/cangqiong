package com.controller.admin;

import com.context.BaseContext;
import com.dto.AddSetmealDTO;
import com.dto.SetmealListDTO;
import com.dto.UpdateSetmealDTO;
import com.result.Result;
import com.service.SetmealService;
import com.vo.PageSetmealVO;
import com.vo.SelectSetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
@Api(tags = "管理端套餐接口相关操作")
public class SetmealController {

    @Autowired
    private   SetmealService setmealService;
    @ApiOperation("套餐分页查询")
    @GetMapping("/page")
    public Result<PageSetmealVO> page(SetmealListDTO setmealListDTO){
        PageSetmealVO pageSetmealVO=new PageSetmealVO();
        pageSetmealVO =setmealService.page(setmealListDTO);
        return Result.success(pageSetmealVO);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据套餐id查询套餐")
    public Result<SelectSetmealVO> getSetmealById(@PathVariable Long id)
    {
        SelectSetmealVO selectSetmealVO=new SelectSetmealVO();
        selectSetmealVO=setmealService.getSetmealById(id);

        return Result.success(selectSetmealVO);
    }


    @PostMapping()
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache",key = "#addSetmealDTO.getCategoryId()")
    public Result<String> saveSetmeal(@RequestBody AddSetmealDTO addSetmealDTO)
    {
         setmealService.saveSetmeal(addSetmealDTO);

        return Result.success("新增套餐成功");
    }
    @DeleteMapping()
    @ApiOperation("批量删除套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result<String> deleteSetmealWithSetmealDish(@RequestParam() List<Long> ids)
    {
        setmealService.deleteSetmealWithSetmealDish(ids);
         return Result.success("批量删除成功");
    }

    @PostMapping("/status/{status}")
    @ApiOperation("修改套餐状态")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result<String> updateStatus(@PathVariable String status,@RequestParam Long id)
    {

        setmealService.updateStatus(status,id);
        return Result.success("修改套餐状态成功");
    }

    @PutMapping()
    @ApiOperation("修改套餐")
    public Result<String> updateSetmeal(@RequestBody UpdateSetmealDTO updateSetmealDTO)
    {
         setmealService.updateSetmeal(updateSetmealDTO);

        return Result.success("修改套餐成功");
    }

}
