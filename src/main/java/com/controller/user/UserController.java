package com.controller.user;

import com.dto.UserLoginDTO;
import com.entity.User;
import com.properties.JwtProperties;
import com.result.Result;
import com.service.UserService;
import com.untils.JwtUtil;
import com.vo.UserLoginVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
public class UserController {
    @Autowired
    private   JwtUtil jwtUtil;
    @Autowired
    private  JwtProperties jwtProperties;
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO)
    {
        //查询数据库获取当前用户的信息
        User user=new User();
        user= userService.getUser(userLoginDTO);
         //生成jwt令牌
        Map<String,Object>  claims=new HashMap<>();
        claims.put("userId",user.getId());
        String token = jwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        UserLoginVO userLoginVO=new UserLoginVO();
        BeanUtils.copyProperties(user,userLoginVO);
        userLoginVO.setToken(token);
        return Result.success(userLoginVO);
    }
}
