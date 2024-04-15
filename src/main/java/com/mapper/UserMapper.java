package com.mapper;

import com.annotation.AutoFill;
import com.entity.User;
import com.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

     @Select("select * from USER where openid =#{openid}")
    User getByOpenid(String openid);

    void insert(User user);
    @Select("select * from user where id = #{id}")
    User getById(Long userId);
}
