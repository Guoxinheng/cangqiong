package com.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.constant.dto.UserLoginDTO;
import com.entity.User;
import com.exception.UserLoginException;
import com.mapper.UserMapper;
import com.properties.WeChatProperties;
import com.service.UserService;
import com.untils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.constant.MessageConstant.User_Login_Error;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;
    public static final String WX_LOGIN="https://api.weixin.qq.com/sns/jscode2session";
    @Override
    public User getUser(UserLoginDTO userLoginDTO) {
       //调用微信接口服务，获取openid
      String openid= getOpenid(userLoginDTO.getCode());
        //没有openid则说明登录失败
        if (openid==null){
            throw new UserLoginException(User_Login_Error);
        }
         //根据openid查询数据库，判断用户是否存在
        User user = userMapper.getByOpenid(openid);
       //如果没有该用户就创建该用户
        if (user==null){
            user=new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            userMapper.insert(user);
            return user;
        }
        return user;
    }


    private String getOpenid(String code) {
        Map<String,String> map=new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
