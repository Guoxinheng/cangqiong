package com.controller.user;

import com.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
public class ShopController {
    @Autowired
    private   RedisTemplate redisTemplate;

    @GetMapping("/status")
    @ApiOperation("店铺营业获取")
    public Result<Integer> shopStatus()
    {
        Integer status = (Integer) redisTemplate.opsForValue().get("status");
        return Result.success(status);
    }
}
