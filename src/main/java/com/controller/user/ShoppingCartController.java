package com.controller.user;



import com.constant.dto.ShoppingCartDTO;
import com.entity.ShoppingCart;
import com.result.Result;
import com.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController("userShoppingCart")
@RequestMapping("/user/shoppingCart")
@Api(tags = "购物车接口")
public class ShoppingCartController {
  @Autowired
  private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    @ApiOperation("购物车添加操作")
    public Result<String> addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO)
    {

        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success("添加成功");
    }
   @GetMapping("/list")
   @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> getShoppingCartList()
   {
       List<ShoppingCart> list=new ArrayList<>();
       list=shoppingCartService.getShoppingList();

       return  Result.success(list);
   }

   @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result<String> cleanShoppingCart()
   {
       shoppingCartService.cleanShoppingCart();
       return Result.success("清空购物车成功");
   }
   @PostMapping("/sub")
    @ApiOperation("删除购物车某个商品")
    public Result<String> deleteShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO)
   {
       shoppingCartService.deleteShoppingCart(shoppingCartDTO);
        return Result.success("删除成功");
   }

}
