package com.mapper;

import com.annotation.AutoFill;
import com.entity.User;
import com.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

     @Select("select * from USER where openid =#{openid}")
    User getByOpenid(String openid);

    void insert(User user);
    @Select("select * from user where id = #{id}")
    User getById(Long userId);

    /**
     * 获取指定时间段内的用户数量。
     *
     * @param beginTime 开始时间，指定查询的起始时间点。
     * @param endTime 结束时间，指定查询的结束时间点。
     * @return 返回在指定时间范围内的用户数量。
     */
    Integer getUserAmount(@Param("beginTime") LocalDateTime beginTime,@Param("endTime") LocalDateTime endTime);
}
