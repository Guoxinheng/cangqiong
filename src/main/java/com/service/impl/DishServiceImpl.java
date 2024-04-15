package com.service.impl;

import com.dto.AddDishDTO;
import com.dto.PageDishDTO;
import com.dto.UpdateDishAndDishFlavor;
import com.entity.Dish;
import com.entity.DishFlavor;
import com.exception.BaseException;
import com.exception.DishStatusException;
import com.exception.SetmealDishDeleteError;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mapper.CategoryMapper;
import com.mapper.DishFlavorMapper;
import com.mapper.DishMapper;
import com.mapper.SetmealDishMapper;
import com.service.DishService;
import com.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.constant.MessageConstant.Dish_Status_Error;
import static com.constant.MessageConstant.Setmeal_Status_Error;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private   DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public void save(AddDishDTO addDishDTO) {
        //新增菜品
        Dish dish=new Dish();
        BeanUtils.copyProperties(addDishDTO,dish);
        dishMapper.save(dish);
        //新增菜品对应的口味

        //获取insert语句生成的主键值
        Long dishId = dish.getId();

        List<DishFlavor> flavors = addDishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //向口味表插入n条数据
            dishFlavorMapper.save(flavors);
        }

    }
    @Override
    public PageDishVO getpage(PageDishDTO pageDishDTO) {
         PageHelper.startPage(pageDishDTO.getPage(),pageDishDTO.getPageSize());
         Page<DishVO> page=dishMapper.getpage(pageDishDTO);
         PageDishVO pageDishVO=new PageDishVO();
         pageDishVO.setTotal(page.getTotal());
         pageDishVO.setRecords(page.getResult());
         return pageDishVO;

    }

    @Override
    public List<DishVO> list(Long categoryId) {
        return null;
    }



    @Override
    @Transactional
    public void delete(List<Long> ids) {
        //查询当前菜品是否在起售，起售不可以删除
        for (Long id : ids) {
           Dish dish=new Dish();
            dish= dishMapper.selcectById(id);
            if (dish.getStatus()==1){
                throw new DishStatusException(Dish_Status_Error);
            }
        }
        //查询当前菜品是否关联套餐，套餐关联了，不可以删除
         for (Long id : ids)
         {
             List<Long> setmealIds=setmealDishMapper.selectSetmealIdsByDishId(id);
             if (setmealIds!=null&&setmealIds.size()>0){
                 throw new SetmealDishDeleteError(Setmeal_Status_Error);
             }
         }

        //删除菜品

        dishMapper.delete(ids);
        //删除菜品所对应的口味
        for (Long id : ids) {
            dishFlavorMapper.delete(id);
        }
    }
    //根据id修改菜品起售停售
    @Override
    public void updateStatus(Integer status, Long id) {
        dishMapper.updateStatus(status,id);
    }

    @Override
    @Transactional
    public void updateDishAndDishFlavor(UpdateDishAndDishFlavor updateDishAndDishFlavor) {
        //修改菜品
        Dish dish=new Dish();
        BeanUtils.copyProperties(updateDishAndDishFlavor,dish);
        dishMapper.updateDish(dish);
        //先清空里面的数据再进行修改
        dishFlavorMapper.delete(dish.getId());
        //修改菜品所对应的口味
        List<DishFlavor> flavors= updateDishAndDishFlavor.getFlavors();
//        if (flavors!=null&&flavors.size()>0){
//            flavors.forEach(dishFlavor -> {
//                dishFlavor.setDishId(dish.getId());
//            });
//            dishFlavorMapper.save(flavors);
//        }
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dish.getId());
            dishFlavorMapper.saveFlavor(flavor);
        }
    }

    @Override
    public DishAndDishFlavorVO selcectById(Long id) {
        DishAndDishFlavorVO dishAndDishFlavorVO=new DishAndDishFlavorVO();
        //根据id查询菜品信息
         Dish dish=  dishMapper.selcectById(id);
        //根据id查询菜品对应的口味
        List<DishFlavor> flavors= dishFlavorMapper.selectById(id);
        BeanUtils.copyProperties(dish,dishAndDishFlavorVO);
        dishAndDishFlavorVO.setFlavors(flavors);
        return dishAndDishFlavorVO;
    }

    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);
            String categoryName =  categoryMapper.getCategoryname(d.getCategoryId());
            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());
            dishVO.setCategoryName(categoryName);
            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    @Override
    public List<Dish> getDishbyCategoryId(Long categoryId) {
       List<Dish>  dishList=new ArrayList<>();
        dishList= dishMapper.getDishbyCategoryId(categoryId);

        return dishList;
    }


}
