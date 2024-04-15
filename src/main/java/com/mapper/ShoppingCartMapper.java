package com.mapper;


import com.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;


@Mapper
public interface ShoppingCartMapper {


  @Update("update shopping_cart set number=#{number} where dish_id=#{dishId}")
  void updateByDishId(ShoppingCart shoppingCart);
  @Update("update shopping_cart set number=#{number} where setmeal_id=#{setmealId}")
  void updateBySetmealId(ShoppingCart shoppingCart);

  void saveShoppingCart(ShoppingCart shoppingCart);
  ShoppingCart getShoppingCartList(ShoppingCart shoppingCart);

  @Select("select * from shopping_cart where user_id=#{userId}")
  List<ShoppingCart> getShoppingCartListByUserId(Long userId);
  @Delete("delete from shopping_cart where user_id=#{userId}")
  void cleanShoppingCart(Long userId);
  //删除购物车中某一个商品
  void deleteShoppingCart(ShoppingCart shoppingCart);
}
