package com.mapper;

import com.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    void save(OrderDetail orderDetail);

    /**
     * 根据订单ID获取订单详情列表。
     *
     * @param id 订单ID，类型为Long。这是一个必传参数，用于指定需要查询的订单。
     * @return 返回一个OrderDetail对象，包含了指定订单的所有详情。
     */
    @Select("select * from order_detail where order_id=#{id}")
    List<OrderDetail> getListByOrderId(Long id);
}
