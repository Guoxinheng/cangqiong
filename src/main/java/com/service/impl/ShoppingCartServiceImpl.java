package com.service.impl;


import com.context.BaseContext;
import com.constant.dto.ShoppingCartDTO;
import com.entity.Dish;
import com.entity.Setmeal;
import com.entity.ShoppingCart;
import com.mapper.DishMapper;
import com.mapper.SetmealMapper;
import com.mapper.ShoppingCartMapper;
import com.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
  //添加购物车操作
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        if (shoppingCartDTO.getDishId() != null)
        {
            Long userId = BaseContext.getCurrentId();
            // 判断购物车中是否有该菜品
            ShoppingCart shoppingCart=new ShoppingCart();
            BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
            shoppingCart.setUserId(userId);
            shoppingCart  = shoppingCartMapper.getShoppingCartList(shoppingCart);
            if (shoppingCart != null) {
                // 有则增加数量
                shoppingCart.setNumber(shoppingCart.getNumber() + 1);
                shoppingCartMapper.updateByDishId(shoppingCart);
            } else {
                // 没有则添加
                Dish dish = dishMapper.selcectById(shoppingCartDTO.getDishId());
                System.out.println(dish.getId());
                shoppingCart=new ShoppingCart();
                shoppingCart.setDishId(dish.getId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setNumber(1);
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setUserId(BaseContext.getCurrentId());
                shoppingCart.setDishFlavor(shoppingCartDTO.getDishFlavor());
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCartMapper.saveShoppingCart(shoppingCart);
            }
        }
        if (shoppingCartDTO.getSetmealId() != null)
        {
            Long userId = BaseContext.getCurrentId();
            ShoppingCart shoppingCart=new ShoppingCart();
            BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
            shoppingCart.setUserId(userId);
            shoppingCart  = shoppingCartMapper.getShoppingCartList(shoppingCart);
            if (shoppingCart != null) {
                //有则增加数量
                shoppingCart.setNumber(shoppingCart.getNumber() + 1);
                shoppingCartMapper.updateBySetmealId(shoppingCart);
            } else {
                //没有则添加
                Setmeal setmeal = setmealMapper.getSetmealById(shoppingCartDTO.getSetmealId());
                shoppingCart=new ShoppingCart();
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setUserId(BaseContext.getCurrentId());
                shoppingCart.setSetmealId(setmeal.getId());
                shoppingCart.setNumber(1);
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCartMapper.saveShoppingCart(shoppingCart);
            }
        }

    }
    @Override
    public void deleteShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //只能删除当前用户对应的菜品和套餐
        //删除菜品
        //删除套餐
        if (shoppingCartDTO.getDishId() != null)
        {   ShoppingCart shoppingCart=new ShoppingCart();
            BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart  = shoppingCartMapper.getShoppingCartList(shoppingCart);

            if (shoppingCart.getNumber()>1)
            {
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateByDishId(shoppingCart);
            }
            else
            {
                shoppingCartMapper.deleteShoppingCart(shoppingCart);
            }
        }
        if (shoppingCartDTO.getSetmealId() != null)
        {
            ShoppingCart shoppingCart=new ShoppingCart();
            BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart  = shoppingCartMapper.getShoppingCartList(shoppingCart);

            if (shoppingCart.getNumber()>1)
            {
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateBySetmealId(shoppingCart);
            }
            else
            {
                shoppingCartMapper.deleteShoppingCart(shoppingCart);
            }
        }

    }
    @Override
    public List<ShoppingCart> getShoppingList() {

        List<ShoppingCart> list=new ArrayList<>();
        Long userId=BaseContext.getCurrentId();
        list=shoppingCartMapper.getShoppingCartListByUserId(userId);
        return list;
    }
    @Override
    //清空购物车具体操作
    public void cleanShoppingCart() {
        Long userId=BaseContext.getCurrentId();
        shoppingCartMapper.cleanShoppingCart(userId);
    }


}
