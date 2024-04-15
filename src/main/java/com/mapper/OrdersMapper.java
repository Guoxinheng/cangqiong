package com.mapper;

import com.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface OrdersMapper {
    //保存订单
    void save(Orders order);

    /**
     * 根据订单号和用户id查询订单
     * @param orderNumber
     * @param userId
     */
    @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);
    @Update("update orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time} where number = #{id}")

    void updateStatus(@Param("orderStatus") Integer orderStatus,@Param("orderPaidStatus") Integer orderPaidStatus,@Param("check_out_time") LocalDateTime check_out_time,@Param("id") Long id);
}
